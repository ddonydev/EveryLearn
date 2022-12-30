<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Insert title here</title>
        <link rel="stylesheet" href="/el/resources/css/teacher/detail.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
    </head>

    <body>
        <%@ include file="/WEB-INF/views/common/header.jsp" %>

        <main id="container">
        <br><br>
        <div id="contain-detail">
            <div id="intro-wrap">
                <div class="container mt-3" id="mt-3">
                    <div class="card" style="width:250px; height: 370px;" >
                        <img class="card-img-top" id="teacher-img" src="/el/resources/img/teacher/person.png" alt="Card image"
                            >
                        <div class="card-body">
                            <h4 class="card-title">${teacherNick}</h4>
                            <pre class="card-text">
                                ${teacherDetailList[0].shortIntro}
                            </pre>
                        </div>
                    </div>
                </div>
            </div>
            <div>
                <div id="career">
                    <label for="">경력</label> 
                </div>
                <pre id="main-career">
- 코딩짱잘해 자격증 1급
- KH 프로그래밍 대회 대상 수상
- 네카라쿠배 경력 10년 이상
                </pre>
                <div id="lecture">
                    <label for="">강의</label>
                </div>
                <div id="list-wrap">
                    <div class="lec-list">
                        <img id="lec-img" src="/el/resources/img/teacher/ex.png" alt="">
                        <div id="lec-name">프로그래밍 한 방에 이해하기</div>
                        <div id="teacher-name">박강사</div>
                        <div id="lec-reomm">추천수(200)</div>
                        <div id="lec-price">50,000\</div>
                    </div>
                    <div class="lec-list">
                        <img id="lec-img" src="/el/resources/img/teacher/ex.png" alt="">
                        <div id="lec-name">프로그래밍 한 방에 이해하기</div>
                        <div id="teacher-name">박강사</div>
                        <div id="lec-reomm">추천수(200)</div>
                        <div id="lec-price">50,000\</div>
                    </div>
                    <div class="lec-list">
                        <img id="lec-img" src="/el/resources/img/teacher/ex.png" alt="">
                        <div id="lec-name">프로그래밍 한 방에 이해하기</div>
                        <div id="teacher-name">박강사</div>
                        <div id="lec-reomm">추천수(200)</div>
                        <div id="lec-price">50,000\</div>
                    </div>
                    <div class="lec-list">
                        <img id="lec-img" src="/el/resources/img/teacher/ex.png" alt="">
                        <div id="lec-name">프로그래밍 한 방에 이해하기</div>
                        <div id="teacher-name">박강사</div>
                        <div id="lec-reomm">추천수(200)</div>
                        <div id="lec-price">50,000\</div>
                    </div>
                    <div class="lec-list">
                        <img id="lec-img" src="/el/resources/img/teacher/ex.png" alt="">
                        <div id="lec-name">프로그래밍 한 방에 이해하기</div>
                        <div id="teacher-name">박강사</div>
                        <div id="lec-reomm">추천수(200)</div>
                        <div id="lec-price">50,000\</div>
                    </div>

                </div>
                
            </div>


        </main>
        <%@ include file="/WEB-INF/views/common/footer.jsp" %>

    </body>

    </html>