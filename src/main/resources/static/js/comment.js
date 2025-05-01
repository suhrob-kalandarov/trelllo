document.addEventListener('DOMContentLoaded', function() {
    // Scroll to bottom of comments list
    const commentsList = document.querySelector('.comments-list');
    commentsList.scrollTop = commentsList.scrollHeight;

    // Focus comment input when page loads
    document.querySelector('.comment-input').focus();
});
