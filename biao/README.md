### 交易处理API接口文档

#### 1、获取订单号 

> 在买入、卖出的时候需要获取这个订单号，需要传入订单号。

**接口**：/trade/getOrderNo

------

**参数描述:**

无入参

------

**返回结果:**

| 序号 | 参数      | 类型      | 描述                         |
| ---- | --------- | --------- | ---------------------------- |
| 1    | userId    | String    | NULL                         |
| 2    | orderNo   | String    | 订单号（调用获取买入、卖出） |
| 3    | volume    | Double(8) | NULL                         |
| 4    | price     | Double(8) | NULL                         |
| 5    | coinMain  | String    | NULL                         |
| 6    | coinOther | String    | NULL                         |

#### 2、买入、卖出

> 买入、卖出的入参与出参完全一致。

**买入接口：**/trade/buyIn

**卖出接口：**/trade/sellOut

------

**参数描述：**

| 序号 | 参数      | 类型        | 描述                         |
| ---- | --------- | ----------- | ---------------------------- |
| 1    | userId    | String *    | 用户ID;                      |
| 2    | orderNo   | String *    | 订单号（调用获取订单号接口） |
| 3    | volume    | Double(8) * | 数量(大于0)                  |
| 4    | price     | Double(8) * | 单价(大于0)                  |
| 5    | coinMain  | String *    | 主交易区币总                 |
| 6    | coinOther | String *    | 交易对币总                   |

**示例：**

```json
{
     "userId":"189158903743909888",
     "orderNo":"1234ww",
     "volume":30,
     "price":0.133225,
     "coinMain":"USDT",
     "coinOther":"HT"
}
```

------

**返回结果:**

暂无描述（根据需求确定）