document.addEventListener('DOMContentLoaded', function() {
    // Initialize Sortable
    const positionList = document.getElementById('positionList');

    if (positionList) {
        const sortable = new Sortable(positionList, {
            animation: 150,
            handle: '.position-handle',
            ghostClass: 'dragging',
            onEnd: function(evt) {
                // Update position numbers
                updatePositionNumbers();
            }
        });
    }

    // Function to update position numbers
    function updatePositionNumbers() {
        const items = document.querySelectorAll('.position-item');

        items.forEach((item, index) => {
            const positionNumber = item.querySelector('.position-number');
            if (positionNumber) {
                positionNumber.textContent = index + 1;
            }
        });
    }

    // Save positions button
    const savePositionsBtn = document.getElementById('savePositionsBtn');

    if (savePositionsBtn) {
        savePositionsBtn.addEventListener('click', function() {
            savePositions();
        });
    }

    // Function to save positions
    function savePositions() {
        const items = document.querySelectorAll('.position-item');
        const columnIds = Array.from(items).map(item => item.getAttribute('data-column-id'));

        // Get CSRF token
        const token = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
        const header = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

        // Create form data
        const formData = new FormData();
        columnIds.forEach(id => {
            formData.append('columnIds[]', id);
        });

        // Send AJAX request
        fetch('/positions/update', {
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
                return response.text();
            })
            .then(data => {
                // Show success message
                showNotification('Positions saved successfully', 'success');

                // Reload page after inactive short delay
                setTimeout(() => {
                    window.location.reload();
                }, 1000);
            })
            .catch(error => {
                console.error('Error saving positions:', error);
                showNotification('Error saving positions. Please try again.', 'danger');
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
});