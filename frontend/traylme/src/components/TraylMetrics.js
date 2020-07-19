import React from 'react';
import moment from 'moment';
import axios from 'axios';
import { LineChart, Line, CartesianGrid, XAxis, YAxis, Tooltip } from 'recharts';

const HOST = (process.env.NODE_ENV === 'development') ? 'http://localhost:8080' : 'https://trayl.me';

class TraylMetrics extends React.Component {

  constructor(props) {
    super(props);
    this.state = {data: []};
  }

  fetchMetrics() {
      axios
        .get(HOST + '/url_recent_hits/' + this.props.urlId)
        .then(res => {
          const data = res.data.sort((a,b) => Date.parse(a.accessDate) - Date.parse(b.accessDate)).map((r) => {
            return {
              name: moment(r.accessDate).format("DD/MM"),
              clicks: r.counter,
            }
          });
          this.setState({data: data});
        });
  }
  componentDidMount() {
    this.fetchMetrics();
    this.interval = setInterval(() => this.fetchMetrics(), 10000);
  }

  componentWillUnmount() {
      clearInterval(this.interval);
  }


  render() {
    if (typeof this.state.data === 'undefined' || this.state.data.length === 0) {
      return (<div><p>never clicked...</p></div>);
    }
    return (
      <div className="container">
        <LineChart width={400} height={150} data={this.state.data}  margin={{ top: 5, right: 0, bottom: 10, left: 0 }}>
          <Line type="monotone" dataKey="clicks" stroke="#8884d8" activeDot={{ r: 8 }}/>
          <CartesianGrid stroke="#ccc" />
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
        </LineChart>
      </div>
    );
  }
}

export default TraylMetrics;
