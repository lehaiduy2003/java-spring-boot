// Add interactivity if needed
document.addEventListener('DOMContentLoaded', function () {
    console.log('Page loaded!');
    // Example: Add event listeners or dynamic behavior here
});

const loginForm = document.getElementById('loginForm');

// Ignore default form submission behavior
loginForm.addEventListener('submit', function (e) {
    e.preventDefault();
});
const submitButton = document.getElementById('formSubmitButton');

// Add event listener to submit button for fetching sign-in API
submitButton.addEventListener('click', function () {
    fetch('api/v1/auth/sign-in', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: document.getElementById('email').value,
            password: document.getElementById('password').value
        })
    }).then(response => response.json())
    .then(data => {
        console.log(data);
        window.location.href = 'home';
    })
    .catch(error => console.error(error));
})