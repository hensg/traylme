import React from 'react';
import './Form.css';

class Form extends React.Component {

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleSubmit(e) {
    this.props.onSubmit(e);
  }

  handleChange(e) {
    this.props.onInputChange(e.target.value);
  }

  render() {
    return (
      <form className="Form" onSubmit={this.handleSubmit}>
				<input
						type="text"
						className="form-input"
						value={this.props.inputValue}
						onChange={this.handleChange}
						placeholder="Type your URL & press enter..."
						title="press enter"
				/>
        {this.props.error &&
          <p className="form-error-msg">{this.props.error}</p>
        }
      </form>
    );
  }
}

export default Form;
