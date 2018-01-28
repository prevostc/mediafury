import React from 'react';
import { Admin, Resource } from 'admin-on-rest';
import { restClient, authClient } from './apiClients';
import { MovieList } from './movies/movies';

const App = () => (
    <Admin restClient={restClient} authClient={authClient}>
        <Resource name="movies" list={MovieList} />
    </Admin>
);

export default App;