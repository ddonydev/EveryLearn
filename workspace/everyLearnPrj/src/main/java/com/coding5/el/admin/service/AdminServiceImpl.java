package com.coding5.el.admin.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.coding5.el.admin.dao.AdminDao;
import com.coding5.el.admin.vo.AdminVo;
import com.coding5.el.common.page.PageVo;
import com.coding5.el.common.vo.SearchVo;
@Service
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	private SqlSessionTemplate sst;
	
	@Autowired
	private AdminDao adminDao;
	
	@Autowired
	private BCryptPasswordEncoder pwdEnc;
	
	/**
	 * 로그인
	 */
	@Override
	public AdminVo login(AdminVo vo) {
		
		AdminVo dbAdmin = adminDao.selectOneAdminById(sst, vo.getId());
		
		if(dbAdmin == null) return null;

		
		String rawPassword = vo.getPwd();
		String encodedPassword = dbAdmin.getPwd();
		
		if(!pwdEnc.matches(rawPassword, encodedPassword)) return null;
		
		// 암호화 전 비번 넣어주기
		dbAdmin.setRawPwd(rawPassword);

		
		return dbAdmin;
		
	}
	
	/**
	 * 중복체크
	 */
	@Override
	public String dupCheck(AdminVo vo) {
		AdminVo adminMember = adminDao.selectOneAdminByVo(sst, vo);
		
		String result = "";
		
		if(adminMember != null) {
			return result;
		}
		
		return result = "able";
	}
	
	/**
	 * 관리자 등록
	 */
	@Override
	public int join(AdminVo vo) {
		
		// 암호화
		vo.encode(pwdEnc);
		
		return adminDao.insertAdminOne(sst,vo);
	}
	
	
	/**
	 * 관리자 내 정보 수정
	 */
	@Override
	public AdminVo myInfoModify(AdminVo vo) {
		
		// 비밀번호가 넣이 아니라면 암호화 진행
		if(vo.getPwd().length() != 0) {
			vo.encode(pwdEnc);	
		} 	
	
		// 정보 수정
		int result = adminDao.updateAdmin(sst, vo);
		
		if(result != 1) {
			return null;	
		}
		
		return adminDao.selectOneAdminByVo(sst, vo);
	}

	
	/**
	 * 관리자 총수
	 */
	@Override
	public int selectAdminCount(SearchVo svo) {	
		
		return adminDao.selectAdminCount(sst,svo);
	}
	
	/**
	 * 관리자 리스트 가져오기
	 */
	@Override
	public List<AdminVo> selectAdminList(PageVo pv, SearchVo svo) {
		
		return adminDao.selectAdminList(sst,pv, svo);
	}
	
	/**
	 * 관리자 상세조회
	 */
	@Override
	public AdminVo adminDetail(String no) {
		
		return adminDao.selectOneAdminByNo(sst,no);
	}
	
	/**
	 *	마스터 관리자 정보수정 
	 */
	@Override
	public int adminModify(AdminVo vo) {
			
		
		return adminDao.updateAdmin(sst, vo);
	}


}
