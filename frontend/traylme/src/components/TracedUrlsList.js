import React from 'react';
import './TracedUrlsList.css';
import { CopyToClipboard } from 'react-copy-to-clipboard';

const env = process.env.NODE_ENV || 'development'

class TracedUrlsList extends React.Component {

  render() {
    const host = (env === 'development') ? 'http://localhost:8081/' : 'https://trayl.me/'

    const formatOriginalUrl = (originalUrl) => {
      if (originalUrl.length > 50) {
        return originalUrl.substring(0, 50) + "...";
      }
      return originalUrl;
    }

    const itemListLI = this.props.urlList.map((item) =>
      <li key={item.id}>
        <div className="url-detail-grid">
          <div className="url-and-copy">
            <a target="noopener" href={host + item.shortedPath} className="url-detail-traceable">
              {host + item.shortedPath}
            </a>
            <CopyToClipboard text={host + item.shortedPath}>
              <input className="copy-to-clip" type="button" value="copy"/>
            </CopyToClipboard>
          </div>
          <span className="url-detail-original">{formatOriginalUrl(item.originalUrl)}</span>
          <div className="url-detail-metrics">
            <span>{item.urlMetrics}</span>
          </div>
        </div>
      </li>
    );

    return (
        <ul id="my-traced-urls" className="TracedUrlsList">
          {itemListLI }
        </ul>
    );
  }
};

export default TracedUrlsList;
