package com.biao.fileserver.service;

import com.biao.fileserver.domain.Card;
import com.biao.fileserver.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CardServiceImpl implements CardService {

    @Autowired
    public CardRepository cardRepository;

    @Override
    public Card saveCard(Card file) {
        return cardRepository.save(file);
    }

    @Override
    public void removeCard(String id) {
        cardRepository.deleteById(id);
    }

    @Override
    public Optional<Card> getCardById(String id) {
        return cardRepository.findById(id);
    }

    @Override
    public List<Card> listCardByPage(int pageIndex, int pageSize) {
        return null;
    }

/*	@Override
	public Optional<Card> getFileById(String id) {
		return cardRepository.findById(id);
	}

	@Override
	public List<Card> listFilesByPage(int pageIndex, int pageSize) {
		Page<Card> page = null;
		List<Card> list = null;
		
		Sort sort = new Sort(Direction.DESC,"uploadDate"); 
		Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
		
		page = cardRepository.findAll(pageable);
		list = page.getContent();
		return list;
	}*/
}
