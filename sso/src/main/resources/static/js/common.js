// API

const endpoints = {
    registered: "/login?registered",
    unauthorized: "/login?unauthorized",
    users: "/users",
    fileDownload: "/file/download",
    sessions: "/sessions",
    emailVerify: "/email/verify",
}

function withCsrfToken(headers) {
    const csrfToken = document.querySelector("meta[name='csrfToken']").content;
    const csrfTokenHeaderName = document.querySelector("meta[name='csrfHeaderName']").content;
    if (csrfToken) {
        headers.set(csrfTokenHeaderName, csrfToken);
    }
    return headers;
}

function handleUnauthorized(response) {
    if (response.status === 401) {
        location.assign(endpoints.unauthorized)
    }
    return response;
}

function renderErrors(errorsContainer, error) {
    errorsContainer.removeAttribute("hidden");
    errorsContainer.innerHTML = null;
    if (error.reason instanceof Array) {
        for (let reason of error.reason) {
            errorsContainer.innerHTML += `
                <div class="error-container row">
                    <span class="material-symbols-outlined error-container-icon">error</span>
                    <span class="error-container-text">${Object.values(reason)}</span>
                </div>`;
        }
    } else {
        errorsContainer.innerHTML += `
            <div class="error-container row">
                <span class="material-symbols-outlined error-container-icon">error</span>
                <span class="error-container-text">${error.reason}</span>
            </div>`;
    }
}

// COLORS

function getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

function colorRandomize() {
    document.documentElement.style.setProperty('--color-random-1', getRandomColor());
    document.documentElement.style.setProperty('--color-random-2', getRandomColor());
}

colorRandomize()

// CHECKBOXES

const checkboxContexts = document.querySelectorAll(".checkbox-context");
checkboxContexts.forEach((checkboxContext) => {
    checkboxContext.addEventListener("click", checkbox);
});

function checkbox(event) {
    event.preventDefault();
    const context = event.currentTarget;
    const checkbox = context.querySelector(".checkbox");
    const checkboxIcon = context.querySelector(".checkbox-icon");

    if (checkbox.hasAttribute("checked")) {
        checkbox.removeAttribute("checked")
        checkboxIcon.textContent = "check_box_outline_blank";
    } else {
        checkbox.setAttribute("checked", "checked")
        checkboxIcon.textContent = "check_box";
    }
}

// COOKIES

function getCookie(name) {
    const cookies = document.cookie.split('; ');
    for (const cookie of cookies) {
        const [cookieName, cookieValue] = cookie.split('=');
        if (cookieName === name) {
            return cookieValue;
        }
    }
    return null;
}

function setCookie(name, value, daysToExpire) {
    const expirationDate = new Date();
    expirationDate.setDate(expirationDate.getDate() + daysToExpire);
    document.cookie = `${name}=${value}; expires=${expirationDate.toUTCString()}; path=/; SameSite=Lax`;
}

// THEMES

let isDarkTheme = true

const themeIcon = document.querySelector("#themeIcon")

const darkTheme = {
    class: "dark",
    icon: "dark_mode"
}
const lightTheme = {
    class: "light",
    icon: "light_mode"
}

const themeCookieName = "theme"
const themeCookieExpirationDays = 1000

function resolveTheme() {
    const themeCookieValue = getCookie(themeCookieName)

    if (themeCookieValue) {
        // from cookie
        if (themeCookieValue === darkTheme.class) {
            isDarkTheme = true
            setDarkTheme()
        } else if (themeCookieValue === lightTheme.class) {
            isDarkTheme = false
            setLightTheme()
        }
    } else {
        // from html
        if (themeIcon.textContent === darkTheme.icon) {
            isDarkTheme = true
            setDarkTheme()
        } else if (themeIcon.textContent === lightTheme.icon) {
            isDarkTheme = false
            setLightTheme()
        }
    }
}

resolveTheme()

function toggleTheme() {
    isDarkTheme ? setLightTheme() : setDarkTheme()
    resolveTheme()
}

function setDarkTheme() {
    const body = document.querySelector("body")
    body.classList.add(darkTheme.class)
    body.classList.remove(lightTheme.class)
    themeIcon.innerText = lightTheme.icon
    setCookie(themeCookieName, darkTheme.class, themeCookieExpirationDays)
}

function setLightTheme() {
    const body = document.querySelector("body")
    body.classList.add(lightTheme.class)
    body.classList.remove(darkTheme.class)
    themeIcon.innerText = darkTheme.icon
    setCookie(themeCookieName, lightTheme.class, themeCookieExpirationDays)
}

// PASSWORD VISIBILITY SWITCHER

const passwordViewerContexts = document.querySelectorAll(".password-viewer-context")
passwordViewerContexts.forEach(context => addSwitchPasswordVisibilityEvent(context))

function addSwitchPasswordVisibilityEvent(context) {
    const input = context.querySelector(".password-input")
    const icon = context.querySelector(".password-viewer-icon")
    icon.addEventListener("click", () => {
        if (input.type === "password") {
            input.type = "text"
            icon.innerText = "visibility_off"
        } else if (input.type === "text") {
            input.type = "password"
            icon.innerText = "visibility"
        }
    })
}
