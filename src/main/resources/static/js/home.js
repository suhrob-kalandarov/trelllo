document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Drag and Drop functionality
    const taskCards = document.querySelectorAll('.task-card');
    const dropZones = document.querySelectorAll('.column-tasks');

    // Add event listeners to task cards
    taskCards.forEach((card, index) => {
        card.addEventListener('dragstart', dragStart);
        card.addEventListener('dragend', dragEnd);

        // Add staggered animation delay
        card.style.animationDelay = `${index * 0.05}s`;

        // Add hover animation
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px)';
            this.style.boxShadow = 'var(--shadow-md)';
            this.style.borderColor = 'rgba(94, 114, 228, 0.3)';
        });

        card.addEventListener('mouseleave', function() {
            this.style.transform = '';
            this.style.boxShadow = 'var(--shadow-sm)';
            this.style.borderColor = 'rgba(226, 232, 240, 0.6)';
        });
    });

    // Add event listeners to drop zones
    dropZones.forEach(zone => {
        zone.addEventListener('dragover', dragOver);
        zone.addEventListener('dragenter', dragEnter);
        zone.addEventListener('dragleave', dragLeave);
        zone.addEventListener('drop', drop);
    });

    // Drag functions
    function dragStart() {
        this.classList.add('dragging');
        setTimeout(() => {
            this.style.opacity = '0.7';
        }, 0);

        // Add visual feedback to all columns
        document.querySelectorAll('.column-tasks').forEach(zone => {
            zone.classList.add('potential-drop-zone');
        });
    }

    function dragEnd() {
        this.classList.remove('dragging');
        this.style.opacity = '';

        // Remove visual feedback from all columns
        document.querySelectorAll('.column-tasks').forEach(zone => {
            zone.classList.remove('potential-drop-zone');
            zone.classList.remove('drop-zone-active');
        });
    }

    function dragOver(e) {
        e.preventDefault();
    }

    function dragEnter(e) {
        e.preventDefault();
        this.classList.add('drop-zone-active');
    }

    function dragLeave() {
        this.classList.remove('drop-zone-active');
    }

    function drop() {
        this.classList.remove('drop-zone-active');

        const draggedCard = document.querySelector('.dragging');
        if (draggedCard) {
            // Get the column ID from the drop zone
            const columnId = this.getAttribute('data-column-id');
            const taskId = draggedCard.getAttribute('data-task-id');

            // Append the card to the new column
            this.appendChild(draggedCard);

            // Add a nice animation effect
            draggedCard.style.animation = 'none';
            draggedCard.offsetHeight; // Trigger reflow
            draggedCard.style.animation = 'fadeIn 0.5s ease-out';

            // Send AJAX request to update the task's column
            updateTaskColumn(taskId, columnId);
        }
    }

    // Function to update task column via AJAX
    function updateTaskColumn(taskId, columnId) {
        // Create form data
        const formData = new FormData();
        formData.append('taskId', taskId);
        formData.append('columnId', columnId);

        // Get CSRF token
        const token = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
        const header = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

        // Send AJAX request
        fetch('/task/move', {
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
                console.log('Task moved successfully:', data);

                // Update task count in column headers
                updateColumnTaskCounts();

                // Show success notification
                showNotification('Task moved successfully', 'success');
            })
            .catch(error => {
                console.error('Error moving task:', error);

                // Show error notification
                showNotification('Error moving task. Please try again.', 'danger');
            });
    }

    // Function to update column task counts
    function updateColumnTaskCounts() {
        const columns = document.querySelectorAll('.column');

        columns.forEach(column => {
            const columnId = column.getAttribute('data-column-id');
            const taskCount = column.querySelectorAll('.task-card').length;
            const countElement = column.querySelector('.column-title-count');

            if (countElement) {
                countElement.textContent = taskCount;
            } else {
                // Create count element if it doesn't exist
                const titleElement = column.querySelector('.column-title');
                if (titleElement) {
                    const countSpan = document.createElement('span');
                    countSpan.className = 'column-title-count';
                    countSpan.textContent = taskCount;
                    titleElement.appendChild(countSpan);
                }
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
        notification.style.background = 'white';
        notification.style.border = `1px solid ${type === 'success' ? 'rgba(72, 187, 120, 0.3)' : 'rgba(245, 101, 101, 0.3)'}`;

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

    // Add animation to columns
    const columns = document.querySelectorAll('.column');
    columns.forEach((column, index) => {
        column.style.opacity = '0';
        column.style.transform = 'translateY(20px)';
        column.style.animationDelay = `${index * 0.1}s`;

        setTimeout(() => {
            column.style.transition = 'all 0.5s ease';
            column.style.opacity = '1';
            column.style.transform = 'translateY(0)';
        }, 100 * index);
    });

    // Initialize column task counts
    updateColumnTaskCounts();

    // Add click effect to action buttons
    document.querySelectorAll('.task-actions .btn').forEach(btn => {
        btn.addEventListener('mousedown', function() {
            this.style.transform = 'scale(0.95)';
        });

        btn.addEventListener('mouseup', function() {
            this.style.transform = '';
        });
    });
});