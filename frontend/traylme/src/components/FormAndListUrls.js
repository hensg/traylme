import React from 'react';
import Form from './Form';
import TracedUrlsList from './TracedUrlsList';
import axios from 'axios';

const VALID_URL_PATTERN = /^(https?:\/\/)?\w+\..{1,2083}$/i;

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
    this.host = (process.env.NODE_ENV === 'development') ? 'http://localhost:8081/' : 'https://trayl.me'
  }

  handleInputChange(inputValue) {
    this.setState({inputValue: inputValue, error: null});
  }

  handleSubmit(event) {
    event.preventDefault();
    if (!VALID_URL_PATTERN.test(this.state.inputValue)) {
      this.setState({ error: 'The URL seems to be invalid, please have a look' });
      return;
    }

    axios
      .post(this.host + '/anonapi/', {
        originalUrl: this.state.inputValue,
      }, {
        withCredentials: true
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
        console.log(err);
        this.setState({ error: err.response.data.errors.originalUrl });
      });
  }

  componentDidMount() {
    axios
      .get(this.host + '/anonapi/', {withCredentials:true})
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
