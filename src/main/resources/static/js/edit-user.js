document.addEventListener('DOMContentLoaded', function() {
    // Add animation to cards
    const cards = document.querySelectorAll('.card');
    cards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';

        setTimeout(() => {
            card.style.transition = 'all 0.5s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, 100 * index);
    });

    // Password toggle visibility
    const togglePassword = document.getElementById('togglePassword');
    const passwordInput = document.getElementById('password');

    if (togglePassword && passwordInput) {
        togglePassword.addEventListener('click', function() {
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);

            // Toggle icon
            const icon = this.querySelector('i');
            icon.classList.toggle('fa-eye');
            icon.classList.toggle('fa-eye-slash');
        });
    }

    // Password strength meter
    if (passwordInput) {
        const strengthMeter = document.getElementById('strengthMeter');
        const strengthText = document.getElementById('strengthText');

        passwordInput.addEventListener('input', function() {
            const password = this.value;
            let strength = 0;
            let message = '';

            // Validate minimum length
            if (password.length > 0 && password.length < 6) {
                this.setCustomValidity('Password must be at least 6 characters');
                strength = 1;
                message = 'Too weak';
            } else {
                this.setCustomValidity('');

                // Calculate password strength
                if (password.length >= 6) strength += 1;
                if (password.length >= 10) strength += 1;
                if (/[A-Z]/.test(password)) strength += 1;
                if (/[0-9]/.test(password)) strength += 1;
                if (/[^A-Za-z0-9]/.test(password)) strength += 1;

                // Set message based on strength
                switch(strength) {
                    case 0:
                        message = 'Password strength';
                        break;
                    case 1:
                    case 2:
                        message = 'Weak';
                        break;
                    case 3:
                        message = 'Medium';
                        break;
                    case 4:
                        message = 'Strong';
                        break;
                    case 5:
                        message = 'Very strong';
                        break;
                }
            }

            // Update UI
            const percentage = (strength / 5) * 100;
            strengthMeter.style.width = `${percentage}%`;

            // Set color based on strength
            let color = '#e2e8f0'; // Default gray
            if (strength === 1 || strength === 2) color = '#f5365c'; // Red for weak
            if (strength === 3) color = '#fb6340'; // Orange for medium
            if (strength === 4) color = '#ffd600'; // Yellow for strong
            if (strength === 5) color = '#2dce89'; // Green for very strong

            strengthMeter.style.backgroundColor = color;
            strengthText.textContent = message;
            strengthText.style.color = color;
        });
    }

    // Avatar upload preview
    const avatarUpload = document.getElementById('avatarUpload');
    const avatarPreview = document.getElementById('avatarPreview');
    const previewContainer = document.getElementById('previewContainer');
    const removeAvatar = document.getElementById('removeAvatar');
    const fileUploadContainer = document.querySelector('.file-upload-container');

    if (avatarUpload && avatarPreview && previewContainer && removeAvatar) {
        avatarUpload.addEventListener('change', function() {
            if (this.files && this.files[0]) {
                const reader = new FileReader();

                reader.onload = function(e) {
                    avatarPreview.src = e.target.result;
                    previewContainer.classList.remove('d-none');
                    fileUploadContainer.classList.add('d-none');
                }

                reader.readAsDataURL(this.files[0]);
            }
        });

        removeAvatar.addEventListener('click', function() {
            avatarUpload.value = '';
            previewContainer.classList.add('d-none');
            fileUploadContainer.classList.remove('d-none');
        });
    }

    // Form submission
    const addUserForm = document.getElementById('addUserForm');
    const saveButton = document.getElementById('saveButton');

    if (addUserForm && saveButton) {
        addUserForm.addEventListener('submit', function(e) {
            // Validate form
            if (!this.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();

                // Show validation messages
                this.classList.add('was-validated');
                return;
            }

            // Check if at least one role is selected
            const roleCheckboxes = document.querySelectorAll('input[name="roles"]:checked');
            if (roleCheckboxes.length === 0) {
                e.preventDefault();
                showNotification('Please select at least one role for the user', 'danger');
                return;
            }

            // Add loading state to button
            saveButton.disabled = true;
            saveButton.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i> Creating User...';
        });
    }

    // Account status switches
    const verifiedSwitch = document.getElementById('verifiedSwitch');
    const activeSwitch = document.getElementById('activeSwitch');

    if (verifiedSwitch) {
        verifiedSwitch.addEventListener('change', function() {
            const icon = this.nextElementSibling.querySelector('i');
            if (this.checked) {
                icon.className = 'fas fa-check-circle text-success me-2';
            } else {
                icon.className = 'fas fa-times-circle text-danger me-2';
            }
        });
    }

    if (activeSwitch) {
        activeSwitch.addEventListener('change', function() {
            const icon = this.nextElementSibling.querySelector('i');
            if (this.checked) {
                icon.className = 'fas fa-toggle-on text-success me-2';
            } else {
                icon.className = 'fas fa-toggle-off text-danger me-2';
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
        notification.style.minWidth = '300px';
        notification.style.maxWidth = '400px';
        notification.style.padding = '15px 20px';
        notification.style.borderRadius = 'var(--border-radius)';
        notification.style.boxShadow = 'var(--shadow-lg)';
        notification.style.opacity = '0';
        notification.style.transform = 'translateY(-20px)';
        notification.style.transition = 'all 0.3s ease';

        const icon = type === 'success' ? 'check-circle' : 'exclamation-circle';
        const color = type === 'success' ? 'var(--success)' : 'var(--danger)';

        notification.innerHTML = `
            <div class="d-flex align-items-center">
                <i class="fas fa-${icon} me-3" style="font-size: 1.5rem; color: ${color};"></i>
                <div>
                    <div style="font-weight: 600; margin-bottom: 0.25rem; color: var(--text-dark);">
                        ${type === 'success' ? 'Success!' : 'Error!'}
                    </div>
                    <div style="color: var(--text-light); font-size: 0.9rem;">
                        ${message}
                    </div>
                </div>
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

    // Check for success or error messages in URL params
    const urlParams = new URLSearchParams(window.location.search);
    const successMsg = urlParams.get('success');
    const errorMsg = urlParams.get('error');

    if (successMsg) {
        showNotification(decodeURIComponent(successMsg), 'success');
    }

    if (errorMsg) {
        showNotification(decodeURIComponent(errorMsg), 'danger');
    }
});