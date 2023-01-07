package com.coding5.el.payment.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

import com.coding5.el.lecture.vo.LectureVo;
import com.coding5.el.payment.vo.PaymentVo;

public interface PaymentDao {

	
	
	//결제금액 1% 적립
	public int addPoint(HashMap<String, String> map, SqlSessionTemplate sst);
	
	//주문 추가
	public int addBuy(SqlSessionTemplate sst, List<PaymentVo> payList, LectureVo lecVo);
	
	//주문 정보 추가
	public int addBuyList(SqlSessionTemplate sst, List<PaymentVo> payList);

}
