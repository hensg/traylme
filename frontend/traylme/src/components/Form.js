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
      <div className="form-container">
      <form className="Form" onSubmit={this.handleSubmit}>
				<input
						type="text"
						className="form-input"
						value={this.props.inputValue}
						onChange={this.handleChange}
						placeholder="Type your URL & press enter..."
						title="press enter"
				/>
        <div style={{ justifySelf: "end", display: "block" }}>
          {this.props.error &&
            <span className="form-error-msg">{this.props.error}</span>
          }
          <input
              type="submit"
              className="form-submit"
              value="Submit"
          />
        </div>
      </form>
      </div>
    );
  }
}

export default Form;
