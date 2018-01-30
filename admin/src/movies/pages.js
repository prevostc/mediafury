import React from 'react';
import {
    List, Datagrid, Edit, Create, SimpleForm, TextField, EditButton, DisabledInput, TextInput, LongTextInput,
    CreateButton, Filter, FunctionField
} from 'admin-on-rest';
import { CardActions } from 'material-ui/Card';
import Chip from 'material-ui/Chip';
import FlatButton from 'material-ui/FlatButton';
import NavigationRefresh from 'material-ui/svg-icons/navigation/refresh';
import { CategoriesSelectField } from '../categories/select';


const cardActionStyle = {
    zIndex: 2,
    display: 'inline-block',
    float: 'right',
};
const MovieActions = ({ resource, filters, displayedFilters, filterValues, basePath, showFilter, refresh }) => (
    <CardActions style={cardActionStyle}>
        {filters && React.cloneElement(filters, { resource, showFilter, displayedFilters, filterValues, context: 'button' }) }
        <CreateButton basePath={basePath} />
        <FlatButton primary label="refresh" onClick={refresh} icon={<NavigationRefresh />} />
    </CardActions>
);

const MovieFilter = (props) => (
    <Filter {...props}>
        <TextInput label="Title" source="q" />
    </Filter>
);

export const MovieList = (props) => (
    <List {...props} actions={<MovieActions />} filters={<MovieFilter />} perPage={30}>
        <Datagrid>
            <TextField source="id" sortable={false} />
            <TextField source="title" />
            <FunctionField label="Categories" render={record => record.categories
                ? record.categories.map(category => {
                    return <Chip key={category.name}>{category.name}</Chip>
                })
                : []
            } />
            <TextField source="description" />
            <EditButton basePath="/movies" />
        </Datagrid>
    </List>
);

const MovieTitle = ({ record }) => {
    return <span>Movie {record ? `"${record.title}"` : ''}</span>;
};

export const MovieEdit = (props) => (
    <Edit title={<MovieTitle />} {...props}>
        <SimpleForm>
            <DisabledInput source="id" />
            <TextInput source="title" />
            <LongTextInput source="description" />
            <CategoriesSelectField source="categories" />
        </SimpleForm>
    </Edit>
);


export const MovieCreate = (props) => (
    <Create title="Create a Movie" {...props}>
        <SimpleForm>
            <TextInput source="title" />
            <LongTextInput source="description" />
        </SimpleForm>
    </Create>
);