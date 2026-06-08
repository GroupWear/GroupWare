<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.groupware.dto.EmployeeDTO" %>
<%@ page import="com.groupware.dto.BoardDTO" %>
<%@ page import="com.groupware.dto.BoardFileDTO" %>
<%@ page import="com.groupware.dto.CommentDTO" %>
<%@ page import="java.util.List" %>
<%
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    BoardDTO board = (BoardDTO) request.getAttribute("board");
    List<BoardFileDTO> fileList = (List<BoardFileDTO>) request.getAttribute("fileList");
    List<CommentDTO> commentList = (List<CommentDTO>) request.getAttribute("commentList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= (board != null) ? board.getTitle() : "" %> - GroupWare</title>
<link rel="stylesheet" href="css/board.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="board-container">
        <div class="board-header">
            <h2>
                <% 
                if (board != null) {
                    int bType = board.getBoardType();
                    if (bType == 1) out.print("사내 소식");
                    else if (bType == 2) out.print("자유 게시판");
                    else if (bType == 3) out.print("건의 게시판");
                }
                %>
            </h2>
        </div>

        <% if (board != null) { %>
        <div class="detail-view">
            <h3 style="padding:20px 15px; margin:0; border-bottom:1px solid #ddd;"><%= board.getTitle() %></h3>
            <div class="detail-info">
                <span>작성자: <b><%= board.getEmpName() %></b></span>
                <span>작성일: <%= board.getRegDate() %> | 조회수: <%= board.getHit() %></span>
            </div>
            
            <div class="detail-content"><%= board.getContent() %></div>

            <% if (fileList != null && !fileList.isEmpty()) { %>
                <div class="file-list">
                    <p style="margin-top:0; font-weight:600;">첨부파일</p>
                    <% for (BoardFileDTO file : fileList) { %>
                        <div class="file-item">
                            <span class="icon">📁</span>
                            <a href="fileDownload.do?fileNo=<%= file.getFileNo() %>"><%= file.getOrgName() %></a>
                            <span style="color:#999; font-size:12px;">(<%= file.getFileSize() / 1024 %> KB)</span>
                        </div>
                    <% } %>
                </div>
            <% } %>

            <div style="margin-top:30px; display:flex; justify-content: space-between;">
                <a href="boardList.do?type=<%= board.getBoardType() %>" class="btn btn-outline">목록으로</a>
                
                <div>
                    <% if (loginEmp != null && (board.getEmpNo() == loginEmp.getEmpNo() || "Y".equals(loginEmp.getManager()))) { %>
                        <a href="boardUpdate.do?boardNo=<%= board.getBoardNo() %>" class="btn btn-outline">수정</a>
                        <a href="javascript:void(0);" onclick="deleteConfirm(<%= board.getBoardNo() %>);" class="btn btn-outline" style="color:#e74c3c;">삭제</a>
                    <% } %>
                </div>
            </div>

            <!-- 댓글 영역 -->
            <div class="comment-section">
                <div class="comment-count">댓글 <%= (commentList != null) ? commentList.size() : 0 %>개</div>
                
                <% if (loginEmp != null) { %>
                <form action="commentInsert.do" method="post" class="comment-form">
                    <input type="hidden" name="boardNo" value="<%= board.getBoardNo() %>">
                    <textarea name="content" placeholder="댓글을 입력하세요" required></textarea>
                    <div style="text-align:right;">
                        <button type="submit" class="btn btn-primary">등록</button>
                    </div>
                </form>
                <% } %>

                <div class="comment-list">
                    <% if (commentList != null && !commentList.isEmpty()) { 
                        for (CommentDTO comment : commentList) { %>
                        <div class="comment-item depth-<%= comment.getDepth() %>">
                            <div class="comment-info">
                                <span class="comment-author"><%= comment.getEmpName() %></span>
                                <span class="comment-date"><%= comment.getRegDate() %></span>
                            </div>
                            <div class="comment-content" id="comment-text-<%= comment.getCommentNo() %>"><%= comment.getContent() %></div>
                            
                            <div class="comment-actions">
                                <% if (loginEmp != null) { %>
                                    <a href="javascript:void(0);" onclick="toggleReplyForm(<%= comment.getCommentNo() %>)">답글</a>
                                    <% if (comment.getEmpNo() == loginEmp.getEmpNo() || "Y".equals(loginEmp.getManager())) { %>
                                        <a href="javascript:void(0);" onclick="toggleEditForm(<%= comment.getCommentNo() %>)">수정</a>
                                        <a href="javascript:void(0);" onclick="deleteComment(<%= comment.getCommentNo() %>, <%= board.getBoardNo() %>)" style="color:#e74c3c;">삭제</a>
                                    <% } %>
                                <% } %>
                            </div>

                            <!-- 답글 폼 -->
                            <div id="reply-form-<%= comment.getCommentNo() %>" class="reply-form">
                                <form action="commentInsert.do" method="post" class="comment-form" style="margin:0; padding:10px; background:transparent;">
                                    <input type="hidden" name="boardNo" value="<%= board.getBoardNo() %>">
                                    <input type="hidden" name="parentNo" value="<%= comment.getCommentNo() %>">
                                    <textarea name="content" placeholder="답글을 입력하세요" required></textarea>
                                    <div style="text-align:right;">
                                        <button type="button" class="btn btn-outline" onclick="toggleReplyForm(<%= comment.getCommentNo() %>)" style="margin-right:5px;">취소</button>
                                        <button type="submit" class="btn btn-primary">등록</button>
                                    </div>
                                </form>
                            </div>

                            <!-- 수정 폼 -->
                            <div id="edit-form-<%= comment.getCommentNo() %>" class="edit-form">
                                <form action="commentUpdate.do" method="post" class="comment-form" style="margin:0; padding:10px; background:transparent;">
                                    <input type="hidden" name="commentNo" value="<%= comment.getCommentNo() %>">
                                    <input type="hidden" name="boardNo" value="<%= board.getBoardNo() %>">
                                    <textarea name="content" required><%= comment.getContent() %></textarea>
                                    <div style="text-align:right;">
                                        <button type="button" class="btn btn-outline" onclick="toggleEditForm(<%= comment.getCommentNo() %>)" style="margin-right:5px;">취소</button>
                                        <button type="submit" class="btn btn-primary">수정완료</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    <% } 
                    } else { %>
                        <p style="text-align:center; color:#999; padding:20px;">등록된 댓글이 없습니다.</p>
                    <% } %>
                </div>
            </div>
        </div>
        <% } %>
    </div>

    <script>
        function deleteConfirm(boardNo) {
            if(confirm('정말로 이 게시글을 삭제하시겠습니까?')) {
                location.href = 'boardDelete.do?boardNo=' + boardNo;
            }
        }

        function toggleReplyForm(commentNo) {
            const form = document.getElementById('reply-form-' + commentNo);
            form.style.display = (form.style.display === 'block') ? 'none' : 'block';
        }

        function toggleEditForm(commentNo) {
            const form = document.getElementById('edit-form-' + commentNo);
            form.style.display = (form.style.display === 'block') ? 'none' : 'block';
            
            const text = document.getElementById('comment-text-' + commentNo);
            text.style.display = (form.style.display === 'block') ? 'none' : 'block';
        }

        function deleteComment(commentNo, boardNo) {
            if(confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
                location.href = 'commentDelete.do?commentNo=' + commentNo + '&boardNo=' + boardNo;
            }
        }
    </script>
</body>
</html>
