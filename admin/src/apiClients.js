import { AUTH_LOGIN, AUTH_LOGOUT, AUTH_CHECK, GET_LIST, fetchUtils } from 'admin-on-rest';
import { API_URL } from './config';

const makeUrl = (resource) => `${API_URL}/${resource}`;

export const httpClient = (url, options = {}) => {
    const token = localStorage.getItem('token');
    options.user = {
        authenticated: true,
        token: token
    }
    return fetchUtils.fetchJson(url, options);
};

export const restClient = (type, resource, params) => {
    switch (type) {
        case GET_LIST:
            return httpClient(makeUrl(resource))
                .then((res) => {
                    console.log(res)
                    return { data: res.json.content, total: res.json.totalElements }
                });
        default:
            throw new Error(`${type} not implemented for resource ${resource}`)
    }
};

export const authClient = (type, params) => {
    if (type === AUTH_LOGIN) {
        const { username, password } = params;
        const token = btoa(`${username}:${password}`);
        localStorage.setItem('token', `Basic ${token}`);
        console.log(token)
    } else if (type === AUTH_LOGOUT) {
        localStorage.clear();
        return Promise.resolve();
    } else if (type === AUTH_CHECK) {
        return localStorage.getItem('token') ? Promise.resolve() : Promise.reject();
    }
    return Promise.resolve();
};
