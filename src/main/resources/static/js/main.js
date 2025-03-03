// Toast Notifications
function showToast(message, type = 'info') {
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.textContent = message;
    
    document.body.appendChild(toast);
    
    setTimeout(() => {
        toast.classList.add('show');
    }, 100);
    
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => {
            document.body.removeChild(toast);
        }, 300);
    }, 3000);
}

// Scroll to Top Button
const scrollTop = document.createElement('button');
scrollTop.className = 'scroll-top';
scrollTop.innerHTML = '<i class="bi bi-arrow-up"></i>';
document.body.appendChild(scrollTop);

window.addEventListener('scroll', () => {
    if (window.pageYOffset > 300) {
        scrollTop.classList.add('visible');
    } else {
        scrollTop.classList.remove('visible');
    }
});

scrollTop.addEventListener('click', () => {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
});

// Dark Mode Toggle
const themeToggle = document.createElement('button');
themeToggle.className = 'theme-toggle';
themeToggle.innerHTML = '<i class="bi bi-moon"></i>';
document.body.appendChild(themeToggle);

const isDarkMode = localStorage.getItem('darkMode') === 'true';
if (isDarkMode) {
    document.body.classList.add('dark-mode');
    themeToggle.innerHTML = '<i class="bi bi-sun"></i>';
}

themeToggle.addEventListener('click', () => {
    document.body.classList.toggle('dark-mode');
    const isDark = document.body.classList.contains('dark-mode');
    localStorage.setItem('darkMode', isDark);
    themeToggle.innerHTML = `<i class="bi bi-${isDark ? 'sun' : 'moon'}"></i>`;
});

// Loading States
function setLoading(element) {
    element.classList.add('loading');
}

function removeLoading(element) {
    element.classList.remove('loading');
}

// Form Validation
document.querySelectorAll('form').forEach(form => {
    form.addEventListener('submit', (e) => {
        const requiredFields = form.querySelectorAll('[required]');
        let isValid = true;
        
        requiredFields.forEach(field => {
            if (!field.value.trim()) {
                isValid = false;
                field.classList.add('is-invalid');
            } else {
                field.classList.remove('is-invalid');
            }
        });
        
        if (!isValid) {
            e.preventDefault();
            showToast('Please fill in all required fields', 'error');
        }
    });
});

// Image Loading
document.querySelectorAll('img').forEach(img => {
    img.addEventListener('load', () => {
        img.classList.add('loaded');
    });
    
    img.addEventListener('error', () => {
        img.src = '/images/placeholder.jpg';
    });
});

// Smooth Scroll for Anchor Links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});

// Initialize Tooltips
document.querySelectorAll('[data-bs-toggle="tooltip"]').forEach(element => {
    new bootstrap.Tooltip(element);
});

// Handle Show Card Interactions
document.querySelectorAll('.show-card').forEach(card => {
    card.addEventListener('mouseenter', () => {
        card.style.transform = 'translateY(-10px)';
    });
    
    card.addEventListener('mouseleave', () => {
        card.style.transform = 'translateY(0)';
    });
});

// Handle Search Form
const searchForm = document.querySelector('.search-form');
if (searchForm) {
    searchForm.addEventListener('submit', (e) => {
        const searchInput = searchForm.querySelector('input[name="query"]');
        if (!searchInput || searchInput.value.trim().length < 2) {
            e.preventDefault();
            showToast('Please enter at least 2 characters to search', 'error');
        }
    });
}

// Handle Rating Interactions
document.querySelectorAll('.rating').forEach(rating => {
    rating.addEventListener('mouseenter', () => {
        rating.style.transform = 'scale(1.1)';
    });
    
    rating.addEventListener('mouseleave', () => {
        rating.style.transform = 'scale(1)';
    });
});

// Handle Alert Dismissals
document.querySelectorAll('.alert-dismissible').forEach(alert => {
    const closeButton = alert.querySelector('.btn-close');
    if (closeButton) {
        closeButton.addEventListener('click', () => {
            alert.style.transform = 'translateX(100%)';
            setTimeout(() => alert.remove(), 300);
        });
    }
});

// Handle Responsive Navigation
const navbarToggler = document.querySelector('.navbar-toggler');
if (navbarToggler) {
    navbarToggler.addEventListener('click', () => {
        document.body.classList.toggle('nav-open');
    });
}

// Handle Window Resize
let resizeTimer;
window.addEventListener('resize', () => {
    clearTimeout(resizeTimer);
    resizeTimer = setTimeout(() => {
        if (window.innerWidth > 768) {
            document.body.classList.remove('nav-open');
        }
    }, 250);
}); 