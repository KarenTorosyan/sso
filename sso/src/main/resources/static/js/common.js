const baseUrl = window.location.origin;

const endpoints = {
    users: baseUrl.concat("/users"),
    fileDownload: baseUrl.concat("/file/download"),
    sessions: baseUrl.concat("/sessions")
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
        window.location.href = "/login";
    }
    return response;
}

function renderErrors(errorsContainer, error) {
    errorsContainer.removeAttribute("hidden");
    errorsContainer.innerHTML = null;
    const errors = document.createElement("ul");
    errors.classList.add("errors")
    if (error.reason instanceof Array) {
        for (let reason of error.reason) {
            errors.innerHTML += `
                    <li class="error">
                        <span class="error-text">${Object.values(reason)}</span>
                    </li>`;
        }
    } else {
        errors.innerHTML += `
                    <li class="error">
                        <span class="error-text">${error.reason}</span>
                    </li>`;
    }
    errorsContainer.appendChild(errors);
}
