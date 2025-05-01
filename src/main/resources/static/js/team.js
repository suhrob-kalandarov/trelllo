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

    // Search functionality
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('keyup', function() {
            const searchTerm = this.value.toLowerCase();
            const tableRows = document.querySelectorAll('.team-table tbody tr');

            tableRows.forEach(row => {
                if (row.querySelector('td[colspan]')) {
                    // This is the "No team members found" row, skip it
                    return;
                }

                const username = row.querySelector('td:nth-child(2)').textContent.toLowerCase();
                const email = row.querySelector('td:nth-child(3)').textContent.toLowerCase();
                const roles = row.querySelector('td:nth-child(4)').textContent.toLowerCase();

                if (username.includes(searchTerm) || email.includes(searchTerm) || roles.includes(searchTerm)) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });

            // Check if all rows are hidden
            let allHidden = true;
            tableRows.forEach(row => {
                if (row.style.display !== 'none' && !row.querySelector('td[colspan]')) {
                    allHidden = false;
                }
            });

            // Show or hide the "No results" row
            const noResultsRow = document.querySelector('.team-table .empty-search-results');
            if (allHidden) {
                if (!noResultsRow) {
                    const tbody = document.querySelector('.team-table tbody');
                    const newRow = document.createElement('tr');
                    newRow.className = 'empty-search-results';
                    newRow.innerHTML = `
                        <td colspan="6" class="text-center py-4">
                            <div class="empty-state">
                                <i class="fas fa-search fa-3x mb-3 text-muted"></i>
                                <p>No results found for "${searchTerm}"</p>
                            </div>
                        </td>
                    `;
                    tbody.appendChild(newRow);
                } else {
                    noResultsRow.querySelector('p').textContent = `No results found for "${searchTerm}"`;
                    noResultsRow.style.display = '';
                }
            } else if (noResultsRow) {
                noResultsRow.style.display = 'none';
            }
        });
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
});