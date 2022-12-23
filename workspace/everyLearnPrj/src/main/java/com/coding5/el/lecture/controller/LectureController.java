package com.coding5.el.lecture.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.coding5.el.common.page.PageVo;
import com.coding5.el.common.page.Pagination;
import com.coding5.el.lecture.service.LectureService;
import com.coding5.el.lecture.vo.DetailClassVo;
import com.coding5.el.lecture.vo.LectureVo;
import com.coding5.el.lecture.vo.ReviewVo;

@Controller
@RequestMapping("lecture")
public class LectureController {

	@Autowired
	private LectureService lectureService;

	// 강의 메인리스트
	@GetMapping("main")
	public String main(String pno, Model model) {
		
		// 카운트
		int listCount = lectureService.selectLectureCount();
		int currentPage = Integer.parseInt(pno);
		int pageLimit = 5;
		int boardLimit = 10;
		
		PageVo pv = Pagination.getPageVo(listCount, currentPage, pageLimit, boardLimit);

		List<LectureVo> list = lectureService.getList(pv);
		
		model.addAttribute("pv", pv);
		model.addAttribute("list", list);
		
		return "lecture/main";
	}

	/* 강의 메인리스트 - 카테고리 */

	// 강의 메인리스트 - 카테고리 드로잉
	@GetMapping("main/drawing")
	public String mainDrawing(Model model) {
		List<LectureVo> list = lectureService.getListDrawing();
		model.addAttribute("list", list);
		return "lecture/main";
	}

	// 강의 메인리스트 - 카테고리 요리/베이킹
	@GetMapping("main/cook")
	public String mainCook(Model model) {
		List<LectureVo> list = lectureService.getListCook();
		model.addAttribute("list", list);
		return "lecture/main";
	}

	// 강의 메인리스트 - 카테고리 운동
	@GetMapping("main/workout")
	public String mainWorkout(Model model) {
		List<LectureVo> list = lectureService.getListWorkout();
		model.addAttribute("list", list);
		return "lecture/main";
	}

	// 강의 메인리스트 - 카테고리 음악
	@GetMapping("main/music")
	public String mainMusic(Model model) {
		List<LectureVo> list = lectureService.getListMusic();
		model.addAttribute("list", list);
		return "lecture/main";
	}

	// 강의 메인리스트 - 카테고리 it
	@GetMapping("main/itTech")
	public String mainItTech(Model model) {
		List<LectureVo> list = lectureService.getListItTech();
		model.addAttribute("list", list);
		return "lecture/main";
	}

	// 강의 메인리스트 - 카테고리 it
	@GetMapping("main/language")
	public String mainLanguage(Model model) {
		List<LectureVo> list = lectureService.getListLanguage();
		model.addAttribute("list", list);
		return "lecture/main";
	}

	// 강의 메인리스트 - 카테고리 finance
	@GetMapping("main/finance")
	public String mainFinance(Model model) {
		List<LectureVo> list = lectureService.getListFinance();
		model.addAttribute("list", list);
		return "lecture/main";
	}

	/*******************************************************/

	// 강의 메인리스트 - 검색
	@GetMapping("main/search")
	public String search(String keyword, Model model) {
		
		HashMap<String, String>map = new HashMap<>();
		map.put("keyword", keyword);
		
		List<LectureVo> list = lectureService.searchBoardList(map);
		
		model.addAttribute("list", list);
		return"lecture/main";
	}

	//강의 세부조회
	@RequestMapping("detail")
	public ModelAndView detail (ModelAndView mv,int bno) {
		
		// 클릭시 조회수 증가
		int result = lectureService.increaseCount(bno);
		
		// 상세보기
		if(result>0) {
			LectureVo lvo = lectureService.classDetail(bno);
			
			mv.addObject("lvo",lvo)
			  .setViewName("lecture/lec_detail");
		}else {
			mv.setViewName("common/errorPage");
		}
	
		return mv;

	}

	

	// 강의 상세페이지 - 수강평
	
	  @GetMapping("detail/review") 
	  public String review(int bno, String pno, Model model) {
		  
		// 카운트
		int listCount = lectureService.selectReviewCount(bno);
		int currentPage = Integer.parseInt(pno);
		int pageLimit = 5;
		int boardLimit = 3;
			
		PageVo pv = Pagination.getPageVo(listCount, currentPage, pageLimit, boardLimit);
		
		List<ReviewVo> reviewList = lectureService.selectReview(bno, pv);
		
		LectureVo lvo = lectureService.classDetail(bno);
		
		model.addAttribute("pv", pv);
		model.addAttribute("bno", bno);
		model.addAttribute("lvo", lvo);
		model.addAttribute("reviewList", reviewList);

		return "lecture/lec_review"; 
	   }
	
	
	

	// 본인이 결제한 강의 목차
	@GetMapping("mylist")
	public String mylist() {
		return "lecture/mylist";
	}

	// 강의 내 질문과답변
	@GetMapping("qnalist")
	public String qnalist() {
		return "lecture/qnalist";
	}

	// 질문과답변 작성
	@GetMapping("qnawrite")
	public String qnawrite() {
		return "lecture/qnawrite";
	}

	// 질문과답변 상세페이지
	@GetMapping("qnadetail")
	public String qnadetail() {
		return "lecture/qnadetail";
	}

	// 장바구니
	@GetMapping("cart")
	public String cart() {
		return "lecture/cart";
	}

	// 결제완료
	@GetMapping("completePay")
	public String completePayment() {
		return "lecture/complete_payment";
	}

	// 강의 등록
	@GetMapping("insert")
	public String insert() {
		return "lecture/insert";
	}

	// 강의 등록 - post
	@PostMapping("insert")
	public String insert(LectureVo lvo, MultipartFile upfile, HttpSession session, Model model) {

		lvo.setTeacherNo((String) session.getAttribute("memberNo"));

		int result = lectureService.insertClassOne(lvo);

		if (result == 1) {
			session.setAttribute("alertMsg", "기본 정보 입력 완료.");
			return "redirect:/lecture/insert/detail";
		} else {
			model.addAttribute("errorMsg", " 게시글 등록에 실패하였습니다. ");
			return "common/errorPage";
		}

	}

	// 강의 등록 - 세부(목차)
	@GetMapping("insert/detail")
	public String insertDetail() {
		
		return "lecture/insertDetail";
		/*
		 * mv.addObject("bno", bno) .setViewName("lecture/insertDetail"); //return
		 * "lecture/insertDetail"; return mv;
		 */
	}

	// 강의 등록 - 세부(목차) - post
	@PostMapping("insert/detail")
	public String insertDetail(LectureVo lvo, List<LectureVo> dcList) {

		int result = lectureService.insertClassDetail(lvo, dcList);

		if (result == 1) {
			return "lecture/main";
		} else {
			return "common/errorPage";
		}

	}
}
