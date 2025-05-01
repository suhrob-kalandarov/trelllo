document.addEventListener('DOMContentLoaded', function() {
    // Auto-submit avatar form when file is selected
    const avatarUpload = document.getElementById('avatarUpload');
    if (avatarUpload) {
        avatarUpload.addEventListener('change', function() {
            if (this.files && this.files[0]) {
                // Create a FormData object
                const formData = new FormData();
                formData.append('avatar', this.files[0]);
                
                // Get CSRF token
                const token = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
                const header = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
                
                // Show loading state
                const avatarContainer = document.querySelector('.profile-avatar-container');
                const originalContent = avatarContainer.innerHTML;
                avatarContainer.innerHTML = `
                    <div class="d-flex justify-content-center align-items-center h-100">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">Loading...</span>
                        </div>
                    </div>
                `;
                
                // Send AJAX request
                fetch('/profile/update-avatar', {
                    method: 'POST',
                    body: formData,
                    headers: {
                        [header]: token
                    }
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        // Reload the page to show the new avatar
                        window.location.reload();
                    } else {
                        // Show error
                        showNotification('Failed to update avatar: ' + data.message, 'danger');
                        avatarContainer.innerHTML = originalContent;
                    }
                })
                .catch(error => {
                    console.error('Error updating avatar:', error);
                    showNotification('Error updating avatar. Please try again.', 'danger');
                    avatarContainer.innerHTML = originalContent;
                });
            }
        });
    }
    
    // Function to show notifications
    function showNotification(message, type) {
        // Create notification element
        const notification = document.createElement('div');
        notification.className = `alert alert-${type} notification-toast`;
        notification.style.position = 'fixed';
        notification.style.top = '20px';
        notification.style.right = '20px';
        notification.style.zIndex = '9999';
        notification.style.minWidth = '250px';
        notification.style.padding = '15px 20px';
        notification.style.borderRadius = 'var(--border-radius)';
        notification.style.boxShadow = 'var(--shadow-lg)';
        notification.style.opacity = '0';
        notification.style.transform = 'translateY(-20px)';
        notification.style.transition = 'all 0.3s ease';

        notification.innerHTML = `
            <div class="d-flex align-items-center">
                <i class="fas fa-${type === 'success' ? 'check-circle' : 'exclamation-circle'} me-2"></i>
                <span>${message}</span>
            </div>
        `;

        // Add to document
        document.body.appendChild(notification);

        // Trigger animation
        setTimeout(() => {
            notification.style.opacity = '1';
            notification.style.transform = 'translateY(0)';
        }, 10);

        // Remove after 3 seconds
        setTimeout(() => {
            notification.style.opacity = '0';
            notification.style.transform = 'translateY(-20px)';

            setTimeout(() => {
                document.body.removeChild(notification);
            }, 300);
        }, 3000);
    }
    
    // Add animation to cards
    const cards = document.querySelectorAll('.profile-card, .profile-details-card, .profile-activity-card');
    cards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';

        setTimeout(() => {
            card.style.transition = 'all 0.5s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, 100 * index);
    });
    
    // Password validation
    const passwordForm = document.querySelector('form[action="/profile/change-password"]');
    if (passwordForm) {
        passwordForm.addEventListener('submit', function(event) {
            const password = this.querySelector('input[name="password"]').value;
            const confirmPassword = this.querySelector('input[name="confirmPassword"]').value;
            
            if (password !== confirmPassword) {
                event.preventDefault();
                showNotification('Passwords do not match', 'danger');
            }
        });
    }
});