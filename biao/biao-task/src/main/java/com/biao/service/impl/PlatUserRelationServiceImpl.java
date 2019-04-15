package com.biao.service.impl;

import com.biao.entity.PlatUser;
import com.biao.entity.UserRelation;
import com.biao.enums.UserStatusEnum;
import com.biao.mapper.MkDistributeRuleDao;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.mkcommon.MkCommonUserRelationDao;
import com.biao.service.PlatUserRelationService;
import com.biao.service.SerialCodeService;
import com.biao.util.SnowFlake;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlatUserRelationServiceImpl implements PlatUserRelationService {

    @Autowired
    private MkCommonUserRelationDao mkCommonUserRelationDao;

    @Autowired
    private PlatUserDao platUserDao;

    @Autowired
    private SerialCodeService serialCodeService;

    @Autowired
    private MkDistributeRuleDao mkDistributeRuleDao;

    @Override
    public void initPlatUserRelation() {
        List<Map<String,Object>> userList = mkDistributeRuleDao.getUserTreeId();

        Map<String, String> userReferMap = new HashMap<>();
        Map<String, String> userTreeIdMap = new HashMap<>();

        userList.forEach(uMap ->{
            userReferMap.put(String.valueOf(uMap.get("id")), String.valueOf(uMap.get("refer_id")));
        });

        String topParentUserId = "250686639653916672";
        List<UserRelation> userRelationList1 = platUserDao.findUserRelationById(topParentUserId);

        Optional<UserRelation> parentUserRelation = userRelationList1.stream().findFirst();
        String parentTreeId = userRelationList1.stream().map(e -> e.getTreeId()).collect(Collectors.joining());


        userReferMap.forEach((userId, referId) -> {
                    String curTreeId = userId + ",";
                    String curReferId = referId;
                    while (StringUtils.isNotEmpty(curReferId)){

                        if(topParentUserId.equals(curReferId)){
                            curTreeId = parentTreeId.concat(curTreeId);
                            break;
                        } else{
                            curTreeId = curReferId.concat(",").concat(curTreeId);
                            curReferId = userReferMap.get(curReferId);
                        }
                    }
                    userTreeIdMap.put(userId, curTreeId);
                }
        );


        List<UserRelation> userRelationList = new ArrayList<>();
        userList.forEach(map ->{
            if(StringUtils.isEmpty(String.valueOf(map.get("refer_id")))){
                UserRelation userRelation = new UserRelation();
                userRelation.setId(UUID.randomUUID().toString().replace("-",""));
                userRelation.setUserId(String.valueOf(map.get("id")));
                userRelation.setDeth(1);
                userRelation.setLevel(1);
                userRelation.setParentId(null);
                userRelation.setTopParentId(null);
                userRelation.setTreeId(userTreeIdMap.get(String.valueOf(map.get("id"))));
                userRelation.setUsername(String.valueOf(map.get("username")));
                userRelation.setCreateDate(LocalDateTime.now());
                userRelation.setUpdateDate(LocalDateTime.now());
                userRelationList.add(userRelation);
            }else{
                String userTreeId = userTreeIdMap.get(String.valueOf(map.get("id")));
                String[] userTreedIds = userTreeId.split(",");
                String topParentId = userTreedIds[0];
                int deth = userTreedIds.length;
                int perLevelCount = 1; // 每层记录1个父节点
                int level = ((deth - 1) / perLevelCount) + 1;

                for(int i = level ; i > 0; i--){

                    int end = i == level ? deth : i * perLevelCount;
                    int start = (i - 1) * perLevelCount + 1;
                    StringBuilder curTreeId = new StringBuilder("");
                    for(int j = start - 1; j < end ; j++ ){
                        curTreeId.append(userTreedIds[j]).append(",");
                    }

                    UserRelation userRelation = new UserRelation();
                    userRelation.setId(UUID.randomUUID().toString().replace("-",""));
                    userRelation.setUserId(String.valueOf(map.get("id")));
                    userRelation.setDeth(deth);
                    userRelation.setLevel(i);
                    userRelation.setParentId(String.valueOf(map.get("refer_id")));
                    userRelation.setTopParentId(topParentId);
                    userRelation.setTreeId(curTreeId.toString());
                    userRelation.setUsername(String.valueOf(map.get("username")));
                    userRelation.setCreateDate(LocalDateTime.now());
                    userRelation.setUpdateDate(LocalDateTime.now());
                    userRelationList.add(userRelation);
                }
            }
        });

        platUserDao.batchInsertUserRelation(userRelationList);

    }


    private void initPlatUserRelationCore(){
        String parentId = "230710520934699008"; //父节点（17873947908）
        int number = 20; //父节点下插入20个用户
        String mail_format = "C%s@sina.com"; //mail
        int invite_code_start = 1000; //邀请码初始值

        //生成20个用户
        PlatUser parentUser = platUserDao.findById(parentId);
        PlatUser curParentUser = parentUser;
        for (int i = 1; i <= number; i++){
            PlatUser platUser = new PlatUser();
            platUser.setId(SnowFlake.createSnowFlake().nextIdString());
            platUser.setStatus(Integer.parseInt(UserStatusEnum.USER_NORMAl.getCode()));
            platUser.setUserType(1);
            String prefix =new StringBuilder(platUser.getUserType()).toString() ;
            platUser.setUsername(serialCodeService.generateSerialCode(prefix));
            platUser.setMail(String.format(mail_format,this.getNumberStr(i)));
            platUser.setPassword("");
            platUser.setInviteCode(String.valueOf(invite_code_start + i));
            platUser.setReferInviteCode(curParentUser.getInviteCode());
            platUser.setReferId(curParentUser.getId());
            platUserDao.insert(platUser);
            //生成层级结构
            this.createUserRelation(platUser);
            //当前用户作下一个用户的父级
            curParentUser = platUser;
        }

        //重新生成EGO父节点的用户关系
        String egoTopParentId = "230424809039859712"; //ego父节点
        PlatUser egoTopParentUser = platUserDao.findById(egoTopParentId);
        //以第20个用户为父节点，即最后一个用户
        egoTopParentUser.setReferInviteCode(curParentUser.getInviteCode());
        egoTopParentUser.setReferId(curParentUser.getId());
        platUserDao.updateById(egoTopParentUser);

        //重新生成EGO父节点用户关系
        this.createUserRelation(egoTopParentUser);

        //ego子节点，六个分组
        List<String> egoParentIdList = new ArrayList<>();
        egoParentIdList.add("249920698657673216");
        egoParentIdList.add("250333929297547264");
        egoParentIdList.add("247829549914656768");
        egoParentIdList.add("249327202343194624");
        egoParentIdList.add("250339977773322240");
        egoParentIdList.add("250341019244171264");

        //生成六个分组的用户关系
        for (String id: egoParentIdList) {
            PlatUser platUser = platUserDao.findById(id);
            this.createUserRelation(platUser);
        }
    }


    private String getNumberStr(int number){
        if(number >= 100){
            return String.valueOf(number);
        }else if (number >= 10){
            return "0".concat(String.valueOf(number));
        }else{
            return "00".concat(String.valueOf(number));
        }
    }

    private void createUserRelation(PlatUser platUser){
        List<UserRelation> userRelationList = new ArrayList<>();;
        if(StringUtils.isEmpty(platUser.getReferId())){
            UserRelation userRelation = new UserRelation();
            userRelation.setId(UUID.randomUUID().toString().replace("-",""));
            userRelation.setUserId(platUser.getId());
            userRelation.setDeth(1);
            userRelation.setLevel(1);
            userRelation.setParentId(null);
            userRelation.setTopParentId(null);
            userRelation.setTreeId(platUser.getId() + ",");
            userRelation.setUsername(platUser.getUsername());
            userRelation.setCreateDate(LocalDateTime.now());
            userRelation.setUpdateDate(LocalDateTime.now());
            userRelationList.add(userRelation);
        }else{

            List<UserRelation> userRelationList1 = platUserDao.findUserRelationById(platUser.getReferId());
            if(CollectionUtils.isEmpty(userRelationList1)){
                return ;
            }

            Optional<UserRelation> parentUserRelation = userRelationList1.stream().findFirst();
            String parentTreeId = userRelationList1.stream().map(e -> e.getTreeId()).collect(Collectors.joining());
            String userTreeId = parentTreeId.concat(platUser.getId()).concat(",");
            int deth = parentUserRelation.get().getDeth() + 1;

            int level_size = 1; //每条记录多少个父节点ID
            String[] userTreedIds = userTreeId.split(",");
            int level = ((deth - 1) / level_size) + 1;

            for(int i = level ; i > 0; i--){

                int end = i == level ? deth : i * level_size;
                int start = (i - 1) * level_size + 1;
                StringBuilder curTreeId = new StringBuilder("");
                for(int j = start - 1; j < end ; j++ ){
                    curTreeId.append(userTreedIds[j]).append(",");
                }

                UserRelation userRelation = new UserRelation();
                userRelation.setId(UUID.randomUUID().toString().replace("-",""));
                userRelation.setUserId(platUser.getId());
                userRelation.setDeth(deth);
                userRelation.setLevel(i);
                userRelation.setParentId(parentUserRelation.get().getUserId());
                userRelation.setTopParentId(parentUserRelation.get().getTopParentId() == null ? userRelation.getParentId() : parentUserRelation.get().getTopParentId());
                userRelation.setTreeId(curTreeId.toString());
                userRelation.setUsername(platUser.getUsername());
                userRelation.setCreateDate(LocalDateTime.now());
                userRelation.setUpdateDate(LocalDateTime.now());
                userRelationList.add(userRelation);
            }
        }

        platUserDao.batchInsertUserRelation(userRelationList);
    }
}
