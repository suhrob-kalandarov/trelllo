document.addEventListener('DOMContentLoaded', function() {
    // Preview image before upload
    const imageUpload = document.getElementById('image-upload');
    if (imageUpload) {
        imageUpload.addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    // Check if image preview container exists
                    let imagePreview = document.querySelector('.task-image-preview');
                    let imageContainer = document.querySelector('.image-container');
                    let imagePlaceholder = document.querySelector('.image-placeholder');

                    // If placeholder exists, replace it with image preview
                    if (imagePlaceholder) {
                        const taskImageSection = document.querySelector('.task-image-section');

                        // Create image container and preview
                        imageContainer = document.createElement('div');
                        imageContainer.className = 'image-container';

                        imagePreview = document.createElement('img');
                        imagePreview.className = 'task-image-preview';

                        const overlay = document.createElement('div');
                        overlay.className = 'image-overlay';
                        overlay.innerHTML = `
                            <button type="button" class="btn btn-light" onclick="document.getElementById('image-upload').click()">
                                <i class="fas fa-camera me-1"></i> Change
                            </button>
                        `;

                        imageContainer.appendChild(imagePreview);
                        imageContainer.appendChild(overlay);

                        // Replace placeholder with image container
                        imagePlaceholder.parentNode.replaceChild(imageContainer, imagePlaceholder);
                    }

                    // Update image preview
                    if (imagePreview) {
                        imagePreview.src = e.target.result;
                    }
                };
                reader.readAsDataURL(file);
            }
        });
    }

    // Add animation to the card
    const card = document.querySelector('.card');
    if (card) {
        setTimeout(() => {
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, 100);
    }

    // Initialize tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
});