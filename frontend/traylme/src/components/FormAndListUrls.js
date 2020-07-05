import React from 'react';
import Form from './Form';
import TracedUrlsList from './TracedUrlsList';
import axios from 'axios';

class FormAndListUrls extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      inputValue: '',
      error: null,
      urlList: []
    };
    this.handleInputChange = this.handleInputChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleInputChange(inputValue) {
    this.setState({inputValue: inputValue});
  }

  handleSubmit(event) {
    event.preventDefault();
    axios
      .post('http://localhost:8080/anon_users/123/urls', {
        originalUrl: this.state.inputValue
      })
      .then(res => {
        console.log(res.data);
        this.setState({
          inputValue: '',
          error: null,
          urlList: [res.data].concat(this.state.urlList)
        });
      })
      .catch(err => {
        this.setState({ error: err.response.data.errors.originalUrl });
      });
  }

  componentDidMount() {
    axios
      .get('http://localhost:8080/anon_users/123/urls')
      .then(res => {
        console.log(res.data);
        this.setState({
          inputValue: '',
          error: null,
          urlList: res.data
        });
      });
  }

  render() {
    return (
      <div>
        <Form
          onSubmit={this.handleSubmit}
          inputValue={this.state.inputValue}
          onInputChange={this.handleInputChange}
          error={this.state.error}
        />
        <TracedUrlsList
          urlList={this.state.urlList}
        />
      </div>
    );
  }
}

export default FormAndListUrls;
