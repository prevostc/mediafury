import React from 'react';
import { Admin, Resource } from 'admin-on-rest';
import { restClient, authClient } from './apiClients';
import { MovieList, MovieCreate, MovieEdit, MovieIcon } from './movies/movies';

const App = () => (
    <Admin restClient={restClient} authClient={authClient}>
        <Resource name="movies" list={MovieList} edit={MovieEdit} create={MovieCreate} icon={MovieIcon} />
    </Admin>
);

export default App;