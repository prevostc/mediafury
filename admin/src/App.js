import React from 'react';
import { Admin, Resource } from 'admin-on-rest';
import { restClient, authClient } from './apiClients';
import { MovieList, MovieCreate, MovieEdit } from './movies/pages';
import { MovieIcon } from './movies/icon';
import { CategoryList, CategoryCreate, CategoryEdit } from './categories/pages';
import { CategoryIcon } from './categories/icon';

const App = () => (
    <Admin restClient={restClient} authClient={authClient}>
        <Resource name="movies" list={MovieList} edit={MovieEdit} create={MovieCreate} icon={MovieIcon} />
        <Resource name="categories" list={CategoryList} edit={CategoryEdit} create={CategoryCreate} icon={CategoryIcon} />
    </Admin>
);

export default App;