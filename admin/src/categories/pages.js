import React from 'react';
import {
    List, Datagrid, Edit, Create, SimpleForm, TextField, EditButton, DisabledInput, TextInput,
    CreateButton, Filter
} from 'admin-on-rest';
import { CardActions } from 'material-ui/Card';
import FlatButton from 'material-ui/FlatButton';
import NavigationRefresh from 'material-ui/svg-icons/navigation/refresh';

const cardActionStyle = {
    zIndex: 2,
    display: 'inline-block',
    float: 'right',
};
const CategoryActions = ({ resource, filters, displayedFilters, filterValues, basePath, showFilter, refresh }) => (
    <CardActions style={cardActionStyle}>
        {filters && React.cloneElement(filters, { resource, showFilter, displayedFilters, filterValues, context: 'button' }) }
        <CreateButton basePath={basePath} />
        <FlatButton primary label="refresh" onClick={refresh} icon={<NavigationRefresh />} />
    </CardActions>
);

const CategoryFilter = (props) => (
    <Filter {...props}>
        <TextInput label="Name" source="q" />
    </Filter>
);

export const CategoryList = (props) => (
    <List {...props} actions={<CategoryActions />} filters={<CategoryFilter />} perPage={30}>
        <Datagrid>
            <TextField source="id" sortable={false} />
            <TextField source="name" />
            <EditButton basePath="/categories" />
        </Datagrid>
    </List>
);

const CategoryTitle = ({ record }) => {
    return <span>Category {record ? `"${record.name}"` : ''}</span>;
};

export const CategoryEdit = (props) => (
    <Edit title={<CategoryTitle />} {...props}>
        <SimpleForm>
            <DisabledInput source="id" />
            <TextInput source="name" />
        </SimpleForm>
    </Edit>
);


export const CategoryCreate = (props) => (
    <Create title="Create a Category" {...props}>
        <SimpleForm>
            <TextInput source="name" />
        </SimpleForm>
    </Create>
);