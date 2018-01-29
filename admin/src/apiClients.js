import { AUTH_LOGIN, AUTH_LOGOUT, AUTH_CHECK, GET_LIST, GET_ONE, UPDATE, CREATE, DELETE, fetchUtils } from 'admin-on-rest';
import { API_URL } from './config';

const makeUrl = (resource, params) => {
    const urlParams = Object.entries(params).map(([key, val]) => `${key}=${encodeURIComponent(val)}`).join('&');
    const id = params.id ? `/${params.id}` : '';
    return `${API_URL}/${resource}${id}?${urlParams}`;
}

export const httpClient = (url, options = {}) => {
    const token = localStorage.getItem('token');
    options.user = {
        authenticated: true,
        token: token
    }
    return fetchUtils.fetchJson(url, options);
};

const selfLinkAsId = (entity) => ({...entity, id: entity._links.self.href.split('/').pop() })

const mapSingleResult = res => {
    const mappedResult = { data:  selfLinkAsId(res.json) };
    return mappedResult;
};

export const restClient = (type, resource, params) => {
    switch (type) {
        case GET_LIST:
            let promise;
            const filterKeys = Object.keys(params.filter);
            if (filterKeys.length > 0) {
                promise = httpClient(makeUrl(resource + '/search/search', {
                    page: params.pagination.page - 1,
                    size: params.pagination.perPage,
                    sort: `${params.sort.field},${params.sort.order}`,
                    q: params.filter[filterKeys[0]]
                }))
            } else {
                promise = httpClient(makeUrl(resource, {
                    page: params.pagination.page - 1,
                    size: params.pagination.perPage,
                    sort: `${params.sort.field},${params.sort.order}`,
                }))
            }
            return promise.then((res) => {
                const data = res.json;
                const mappedResult = {
                    data: data._embedded[resource].map(selfLinkAsId),
                    total: data.page.totalElements
                };
                return mappedResult;
            });

        case GET_ONE:
            return httpClient(makeUrl(resource, {
                id: params.id,
            }))
            .then(mapSingleResult)
        case UPDATE:
            return httpClient(makeUrl(resource, {
                id: params.id,
            }), { method:'PUT', body: JSON.stringify(params.data) })
                .then(mapSingleResult)
        case CREATE:
            return httpClient(makeUrl(resource, {
                id: params.id,
            }), { method:'POST', body: JSON.stringify(params.data) })
                .then(mapSingleResult)
        case DELETE:
            return httpClient(makeUrl(resource, {
                id: params.id,
            }), { method:'DELETE', body: JSON.stringify(params.data) })
                .then(mapSingleResult)
        default:
            const err = `${type} not implemented for resource ${resource}`;
            console.error(err)
            throw new Error(err)
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
