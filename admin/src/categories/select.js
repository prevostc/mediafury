import React, { Component } from 'react';
import { GET_LIST, SelectField } from 'admin-on-rest';
import { restClient } from "../apiClients";


export class CategoriesSelectField extends Component {

    constructor(props) {
        super(props)

        this.state = {
            categories: []
        }
    }

    async componentDidMount() {
        const { data: list } = await restClient(GET_LIST, 'categories', {
            pagination: { page: 1, perPage: 10000 },
            sort: { field: 'name', order: 'ASC' },
            filter: {},
        });

        this.setState(state => ({...state, categories: list}))
    }

    render() {
        const choices = this.state.categories;
        const optionRenderer = choice => choice.name;
        return <SelectField {...this.props} choices={choices} optionText={optionRenderer} />
    }
}
