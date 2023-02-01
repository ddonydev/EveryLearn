package com.coding5.el.corp.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coding5.el.common.file.FileUploader;
import com.coding5.el.common.page.PageVo;
import com.coding5.el.common.page.Pagination;
import com.coding5.el.corp.service.CorpMailService;
import com.coding5.el.corp.service.CorpService;
import com.coding5.el.corp.vo.CorpVo;
import com.coding5.el.corp.vo.EmploymentVo;
import com.coding5.el.emp.vo.ApplyVo;
import com.coding5.el.emp.vo.JobPostVo;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("corp")
@Controller
@Slf4j
public class CorpController {

	@Autowired
	private CorpService cs;
	
	@Autowired
	private CorpMailService cms;
	
	// 기업 회원가입(화면)
	@GetMapping("join")
	public String join() {
		return "emp/member/join";
	}
	
	// 기업 회원가입
	@PostMapping("join")
	public String join(CorpVo vo) {
		
		int result = cs.join(vo);
		
		// 결과값이 나오지 않으면 에러페이지로 이동
		if(result != 1) {
			return "common/error";
		}
		
		return "redirect:/corp/login";		
	}
	
	// 아이디 중복체크
	@PostMapping("checkId")
	@ResponseBody
	public String checkId(String id) {
		return cs.checkId(id);
	}
	
	// 기업 로그인(화면)
	@GetMapping("login")
	public String login() {
		return "emp/member/login";
	}
	

	// 기업 로그인
	@PostMapping("login")
	public String login(CorpVo vo, HttpSession session, Model model) {
		
		CorpVo corpMember = cs.login(vo);
		
		// 로그인에 실패하면 로그인 페이지로 이동
		if(corpMember == null) {
			model.addAttribute("msg", "error");
			return "emp/member/login";
		}
		
		// session에 담아준다
		session.setAttribute("corpMember", corpMember);
		return "redirect:/corp/mypage";
	}
	
	// 비밀번호 재설정
	@GetMapping("send-mail")
	public String sendMail() {
		return "emp/member/reset/pwd";
	}
	
	/*
	 * 1. 이메일 입력
	 * 2. 버튼 누르면 인증번호가 담긴 이메일 발송
	 * 3. 이메일에 있는 인증번호를 입력하면 비밀번호 변경 페이지로 이동
	 * 4. 변경할 비밀번호를 입력하면 비밀번호 변경 완료 -> 로그인 페이지로 이동.
	 */
	
	// 비밀번호 재설정 링크 보내기
	@PostMapping("send-mail")
	public String sendMail(@RequestParam(value="id") String id) {
		
		// 아이디가 없거나 입력된 값이 없다면동에러페이지로 이동
		if(id == null || id.equals("")) {
			return "redirect:/common/error";
		}
		
		// 입력된 아이디를 vo에 담아주기
		CorpVo vo = new CorpVo();
		vo.setId(id);
		
		// 메일 전송 서비스 
		int mail = cms.mailService(vo);
		
		return "redirect:/corp/login";
	}
	
	// 비밀번호 재설정 링크(화면)
	@GetMapping("authentication")
	public String resetPwd(@RequestParam(value = "num", defaultValue = "") String num) {
		
		// 랜덤 번호가 없으면 에러페이지로 이동
		if(num == null || num.equals("")) {
			return "redirect:/common/error";
		}
		
		return "emp/member/reset/authentication";
	}
	
	// 비밀번호 재설정 링크
	@PostMapping("authentication")
	public String resetPwd(@RequestParam(value = "num") String num, String pwd) {
		
		// 비밀번호 재설정 서비스
		int result = cs.updatePwd(num, pwd);
		
		// 재설정 링크가 만료 되었다면 -> 토큰 만료 페이지로 이동
		if(result != 1) {
			return "redirect:/expired-token";
		}
		
		return "redirect:/corp/login";
	}
	
	
	// 기업 로그아웃
	@GetMapping("logout")
	public String logout(HttpSession session) {
		// 세션 무효화
		session.invalidate();
		return "emp/member/login";
	}
	
	// 기업 회원 탈퇴
	@PostMapping("quit")
	public String quitCorpMember(HttpSession session) {
		
		// session에 담긴 기업 회원 가져오기
		CorpVo corpMember = (CorpVo) session.getAttribute("corpMember");
		
		// 기업회원이 없다면 로그인 페이지로 이동
		if(corpMember == null) {
			return "redirect:/corp/login";
		}
		
		// 회원 탈퇴 서비스
		int result = cs.quitCorpMember(corpMember);
		
		// 회원 탈퇴가 되지 않았다면 에러페이지로 이동
		if(result != 1) {
			log.info("" + result);	
			return "common/error";
		}
		
		// 되었다면 세션 무효화
		session.invalidate();
		
		return "redirect:/corp/login";
		
	}
	
	// 기업 마이페이지(화면)
	@GetMapping("mypage")
	public String mypage(HttpSession session, Model model) {
		
		// session에 담긴 기업 회원 가져오기
		CorpVo corpMember = (CorpVo) session.getAttribute("corpMember");
		
		// 세션에 담긴 기업 회원이 없다면 로그인 페이지로(마이페이지에선 로그인을 해야 이용 가능)
		if(corpMember == null) {
			return "redirect:/corp/login";
		}
		
		// 기업 회원의 마이페이지 가져오는 서비스
		CorpVo cv = cs.selectMypage(corpMember);
		
		session.setAttribute("corpMember", cv);
		model.addAttribute("cv", cv);
		
		return "emp/mypage/mypage";
	}
	
	// 기업 마이페이지
	@PostMapping("mypage")
	public String mypage(HttpSession session, CorpVo vo) {
		
		// session에 담긴 기업 회원 가져오기
		CorpVo corpMember = (CorpVo) session.getAttribute("corpMember");
		// session에 담긴 회원 no를 가져와서 form 데이터를 가진 vo에 넣어준다
		vo.setNo(corpMember.getNo());
		
		// 회사 로고  이미지 저장
		String logoName = "";
		// 만약 로고 파일이 없다면드업로드
		if(!vo.getLogoFile().isEmpty()) {
			logoName = FileUploader.upload(session, vo.getLogoFile());
			vo.setLogo(logoName);
		}
		
		// 회사 이미지 저장
		String thumbName = "";
		if(!vo.getThumbFile().isEmpty()) {
			thumbName = FileUploader.upload(session, vo.getThumbFile());
			vo.setThumb(thumbName);
		}
		
		// 기업 마이페이지 수정 서비스
		int result = cs.updateCorpInfo(vo);
		
		log.info(vo.toString());
		
		if(result != 1) {
			log.info(vo.toString());
			return "common/error";
		}
		
		return "redirect:/corp/mypage";
	}
	
	// 기업 채용 공고 만들기(화면)
	@GetMapping("register-position")
	public String jobPost(HttpSession session, String no, Model model) {
		
		// session에 담긴 기업 회원 가져오기
		CorpVo corpMember = (CorpVo) session.getAttribute("corpMember");
		
		// session에 담긴 회원 정보가 없다면 로그인 페이지로 이동
		if(corpMember == null) {
			return "redirect:/corp/login";
		}
		
		// 공고 번호가 쿼리스트링으로 입력이 되면
		if(no != null) {
			// 공고를 공고no로 조회해오기 
			EmploymentVo ev = cs.selectEmployment(corpMember, no);
			
			// 해당 회원의 공고가 아니거나, 공고가 없다면 -> 마이페이지로 이동
			if(ev == null) {
				return "redirect:/corp/mypage";
			}
			log.info(ev.getDeadline());
			
			// model객체에 담아 view에 데이터를 전달
			model.addAttribute("ev", ev);
		}
		
		return "emp/mypage/job-post";
	}
	
	// 기업 채용 공고 만들기
	@PostMapping("register-position")
	public String jobPost(EmploymentVo vo, HttpSession session){
		
		// session에 담긴 기업 회원 가져오기
		CorpVo corpMember = (CorpVo) session.getAttribute("corpMember");
		
		// session에 담긴 회원이 없다면
		if(corpMember == null) {
			return "redirect:/corp/login";
		}
		
		// 기업회원의 no를 가져와 공고 vo에 넣어주기
		vo.setCorpNo(corpMember.getNo());
		// 공고 insert 서비스
		int result = cs.insertJobPost(vo);
		
		if(result != 1) {
			return "common/error";
		}
		
		return "redirect:/corp/total";
	}
	
	
	// 채용 공고 세부 조회
	@GetMapping("position")
	public String position(HttpSession session, String no, Model model) {
		
		// session에 담긴 기업 회원 가져오기
		CorpVo corpMember = (CorpVo) session.getAttribute("corpMember");
		
		if(corpMember == null) {
			return "redirect:/corp/login";
		}
		
		// 해당 기업 회원의 공고 조회 서비스
		JobPostVo jp = cs.selectJobPost(corpMember, no);
		
		// 공고가 없다면 마이페이지로 동이동
		if(jp == null) {
			return "redirect:/corp/mypage";
		}
		
		model.addAttribute("jp", jp);
		
		return "emp/member/corp-position";
	}
	
	// 채용 공고 수정하기
	@PostMapping("edit-position")
	public String editPost(HttpSession session, EmploymentVo vo) {
		
		// session에 담긴 기업 회원 가져오기
		CorpVo corpMember = (CorpVo) session.getAttribute("corpMember");
		
		if(corpMember == null) {
			return "redirect:/corp/login";
		}
		
		// 공고 수정하기 서비스
		int result = cs.editJobPost(vo, corpMember);
		
		if(result != 1) {
			log.info(corpMember.toString());
			log.info(vo.toString());
			return "common/error";
		}
		
		return "redirect:/corp/total";
	}
	
	// 채용 공고 지우기
	@GetMapping("delete-position")
	public String jobPost(String no, HttpSession session) {
		
		// 공고 삭제스서비스
		int result = cs.deleteJobPost(no);
		
		if(result != 1) {
			log.info("" + result);
			return "common/error";
		}
		
		return "redirect:/corp/total";
		
	}
	
	// 채용중 페이지
	@GetMapping("hiring")
	public String hiring(Model model, @RequestParam(value="pno", defaultValue = "1") String pno, HttpSession session) {
		
		// session에 담긴 기업 회원 가져오기
		CorpVo corpMember = (CorpVo) session.getAttribute("corpMember");
		
		if(corpMember == null) {
			return "redirect:/corp/login";
		}
		
		// session에 담긴 기업회원No를 corpNo에 담아주기
		String corpNo = corpMember.getNo();
		
		
		// 카운트
		// 전체 채용 공고 개수 가져오기 서비스
		int totalCount = cs.selectHiringCnt(corpNo);
		
		// 현재 페이지 번호(쿼리스트링에서 오는 pno는 String 타입이므로 int로 parsing 해줌)
		int currentPage = Integer.parseInt(pno);
		
		// 한 페이지에 보여지는 페이징 수는 5개
		int pageLimit = 5;
		
		// 한 페이지에 보여지는 게시물 개수는 10개
		int boardLimit = 10;
		
		PageVo pv = Pagination.getPageVo(totalCount, currentPage, pageLimit, boardLimit);
		
		// 채용공고 리스트로 만들어 가져오기
		List<EmploymentVo> list = cs.getList(pv, corpNo);
		
		model.addAttribute("pv", pv);
		model.addAttribute("list", list);
		
		return "emp/mypage/hiring";
	}
	
	// 채용 마감 페이지
	@GetMapping("deadline")
	public String deadLine(Model model, @RequestParam(value="pno", defaultValue = "1") String pno, HttpSession session) {

		// session에 담긴 기업 회원 가져오기
		CorpVo corpMember = (CorpVo) session.getAttribute("corpMember");
		
		if(corpMember == null) {
			return "redirect:/corp/login";
		}
		
		String corpNo = corpMember.getNo();
		
		// 카운트
		int totalCount = cs.selectDeadlineCnt(corpNo);
		int currentPage = Integer.parseInt(pno);
		int pageLimit = 5;
		int boardLimit = 10;
		
		PageVo pv = Pagination.getPageVo(totalCount, currentPage, pageLimit, boardLimit);
		
		List<EmploymentVo> list = cs.getDeadlineList(pv, corpNo);
		log.info(pv.toString());
		model.addAttribute("pv", pv);
		model.addAttribute("list", list);
		
		return "emp/mypage/deadline";
	}
	
	// 채용 전체보기 페이지
	@GetMapping("total")
	public String total(Model model, @RequestParam(value="pno", defaultValue = "1") String pno, HttpSession session) {
		
		// session에 담긴 기업 회원 가져오기
		CorpVo corpMember = (CorpVo) session.getAttribute("corpMember");
		
		if(corpMember == null) {
			return "redirect:/corp/login";
		}
		
		String corpNo = corpMember.getNo();
		
		// 카운트
		int totalCount = cs.selectTotalCnt(corpNo);
		int currentPage = Integer.parseInt(pno);
		int pageLimit = 5;
		int boardLimit = 10;
		
		PageVo pv = Pagination.getPageVo(totalCount, currentPage, pageLimit, boardLimit);
		
		List<EmploymentVo> list = cs.getTotalList(pv, corpNo);
		
		model.addAttribute("pv", pv);
		model.addAttribute("list", list);
		
		return "emp/mypage/total";
	}
	
	// 지원자 현황 페이지
	@GetMapping("applicant")
	public String applicant(Model model, @RequestParam(value="empNo") String empNo, @RequestParam(value="pno", defaultValue = "1") String pno, HttpSession session) {
		
		// session에 담긴 기업 회원 가져오기
		CorpVo corpMember = (CorpVo) session.getAttribute("corpMember");
		
		if(corpMember == null) {
			return "redirect:/corp/login";
		}
		
		// 카운트
		int totalCount = cs.selectApplyCnt(empNo);
		int currentPage = Integer.parseInt(pno);
		int pageLimit = 5;
		int boardLimit = 10;
		
		PageVo pv = Pagination.getPageVo(totalCount, currentPage, pageLimit, boardLimit);
		
		List<ApplyVo> list = cs.applyList(pv, empNo);
		EmploymentVo ev = cs.selectEmployment(corpMember, empNo);
		
		log.info(list.toString());
		
		model.addAttribute("pv", pv);
		model.addAttribute("list", list);
		model.addAttribute("ev", ev);
		
		return "emp/mypage/applicant";
	}

	
}
