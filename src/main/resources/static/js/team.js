document.addEventListener('DOMContentLoaded', function() {
    // Handle delete user modal
    const deleteUserModal = document.getElementById('deleteUserModal');
    if (deleteUserModal) {
        deleteUserModal.addEventListener('show.bs.modal', function(event) {
            // Button that triggered the modal
            const button = event.relatedTarget;

            // Extract info from data attributes
            const userId = button.getAttribute('data-user-id');
            const userName = button.getAttribute('data-user-name');

            // Update the modal's content
            const userNameElement = deleteUserModal.querySelector('#deleteUserName');
            const userIdInput = deleteUserModal.querySelector('#deleteUserId');

            userNameElement.textContent = userName;
            userIdInput.value = userId;
        });
    }

    // Search functionality with enhanced UX
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        // Add focus effects
        searchInput.addEventListener('focus', function() {
            this.parentElement.style.transform = 'translateY(-2px)';
            this.parentElement.style.boxShadow = '0 0 0 3px rgba(94, 114, 228, 0.25)';
        });

        searchInput.addEventListener('blur', function() {
            this.parentElement.style.transform = '';
            this.parentElement.style.boxShadow = '';
        });

        // Search functionality
        searchInput.addEventListener('keyup', function() {
            const searchTerm = this.value.toLowerCase();
            const tableRows = document.querySelectorAll('.team-table tbody tr');
            let visibleCount = 0;

            tableRows.forEach(row => {
                if (row.querySelector('td[colspan]')) {
                    // This is the "No team members found" row, skip it
                    return;
                }

                const username = row.querySelector('td:nth-child(2)').textContent.toLowerCase();
                const email = row.querySelector('td:nth-child(3)').textContent.toLowerCase();
                const status = row.querySelector('td:nth-child(4)').textContent.toLowerCase();
                const roles = row.querySelector('td:nth-child(5)').textContent.toLowerCase();

                if (username.includes(searchTerm) ||
                    email.includes(searchTerm) ||
                    status.includes(searchTerm) ||
                    roles.includes(searchTerm)) {
                    row.style.display = '';
                    visibleCount++;

                    // Highlight the matching text
                    highlightText(row, searchTerm);
                } else {
                    row.style.display = 'none';
                }
            });

            // Show or hide the "No results" row
            updateNoResultsRow(tableRows, visibleCount, searchTerm);
        });
    }

    // Function to highlight matching text
    function highlightText(row, searchTerm) {
        if (!searchTerm) {
            // If search term is empty, remove all highlights
            const highlights = row.querySelectorAll('.highlight');
            highlights.forEach(el => {
                el.outerHTML = el.textContent;
            });
            return;
        }

        const cells = row.querySelectorAll('td');
        cells.forEach(cell => {
            // Skip cells with complex content (like those with badges)
            if (cell.querySelector('.badge') || cell.querySelector('.user-avatar-sm') ||
                cell.querySelector('.action-buttons')) {
                return;
            }

            const originalText = cell.textContent;
            // Remove existing highlights first
            cell.innerHTML = originalText;

            if (originalText.toLowerCase().includes(searchTerm.toLowerCase())) {
                const regex = new RegExp(`(${searchTerm})`, 'gi');
                cell.innerHTML = originalText.replace(regex, '<span class="highlight" style="background-color: rgba(94, 114, 228, 0.2); font-weight: bold;">$1</span>');
            }
        });
    }

    // Function to update the no results row
    function updateNoResultsRow(tableRows, visibleCount, searchTerm) {
        const tbody = document.querySelector('.team-table tbody');
        let noResultsRow = document.querySelector('.empty-search-results');

        if (visibleCount === 0 && searchTerm) {
            if (!noResultsRow) {
                const newRow = document.createElement('tr');
                newRow.className = 'empty-search-results';
                newRow.innerHTML = `
                    <td colspan="6" class="text-center py-4">
                        <div class="empty-state">
                            <i class="fas fa-search"></i>
                            <p>No results found for "${searchTerm}"</p>
                            <button class="btn btn-outline-primary mt-3" id="clearSearch">
                                <i class="fas fa-times me-2"></i> Clear Search
                            </button>
                        </div>
                    </td>
                `;
                tbody.appendChild(newRow);

                // Add event listener to the clear search button
                document.getElementById('clearSearch').addEventListener('click', function() {
                    searchInput.value = '';
                    searchInput.dispatchEvent(new Event('keyup'));
                    searchInput.focus();
                });
            } else {
                noResultsRow.querySelector('p').textContent = `No results found for "${searchTerm}"`;
                noResultsRow.style.display = '';
            }
        } else if (noResultsRow) {
            noResultsRow.style.display = 'none';
        }
    }

    // Add animation to cards
    const cards = document.querySelectorAll('.team-card');
    cards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';

        setTimeout(() => {
            card.style.transition = 'all 0.5s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, 100 * index);
    });

    // Add hover effects to table rows
    const tableRows = document.querySelectorAll('.team-table tbody tr');
    tableRows.forEach(row => {
        if (!row.querySelector('td[colspan]')) { // Skip the "No results" row
            row.addEventListener('mouseenter', function() {
                this.style.backgroundColor = 'rgba(94, 114, 228, 0.05)';
                this.style.transform = 'translateY(-2px)';
                this.style.boxShadow = 'var(--shadow-sm)';
            });

            row.addEventListener('mouseleave', function() {
                this.style.backgroundColor = '';
                this.style.transform = '';
                this.style.boxShadow = '';
            });
        }
    });

    // Add pulse animation to the add user button
    const addUserBtn = document.querySelector('a[href="/user/add-page"]');
    if (addUserBtn) {
        addUserBtn.classList.add('position-relative');

        // Create and add the pulse effect
        const pulse = document.createElement('span');
        pulse.style.position = 'absolute';
        pulse.style.width = '100%';
        pulse.style.height = '100%';
        pulse.style.borderRadius = 'var(--border-radius)';
        pulse.style.border = '2px solid var(--primary)';
        pulse.style.top = '0';
        pulse.style.left = '0';
        pulse.style.animation = 'pulse 2s infinite';

        // Add keyframes for pulse animation
        const style = document.createElement('style');
        style.textContent = `
            @keyframes pulse {
                0% {
                    transform: scale(1);
                    opacity: 0.8;
                }
                70% {
                    transform: scale(1.05);
                    opacity: 0;
                }
                100% {
                    transform: scale(1);
                    opacity: 0;
                }
            }
        `;
        document.head.appendChild(style);

        addUserBtn.appendChild(pulse);
    }

    // Check for URL parameters to show notifications
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('success')) {
        showNotification(urlParams.get('success'), 'success');
    }
    if (urlParams.has('error')) {
        showNotification(urlParams.get('error'), 'danger');
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
        notification.style.border = `1px solid ${type === 'success' ? 'rgba(45, 206, 137, 0.3)' : 'rgba(245, 54, 92, 0.3)'}`;

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
});