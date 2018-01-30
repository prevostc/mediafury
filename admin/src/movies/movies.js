import React from 'react';
import {
    List, Datagrid, Edit, Create, SimpleForm, TextField, EditButton, DisabledInput, TextInput, LongTextInput, DeleteButton,
    CreateButton, Filter, ReferenceManyField, SingleFieldList, ChipField
} from 'admin-on-rest';
import { CardActions } from 'material-ui/Card';
import FlatButton from 'material-ui/FlatButton';
import NavigationRefresh from 'material-ui/svg-icons/navigation/refresh';

import TvIcon from 'material-ui/svg-icons/hardware/tv';
export const MovieIcon = TvIcon;

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
            <TextField source="id" />
            <TextField source="title" />
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