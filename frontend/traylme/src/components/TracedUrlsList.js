import React from 'react';
import moment from 'moment';
import './TracedUrlsList.css';
import TraylMetrics from './TraylMetrics';
import { CopyToClipboard } from 'react-copy-to-clipboard';

const HOST = (process.env.NODE_ENV === 'development') ? 'http://localhost:8081/' : 'https://trayl.me/';

class TracedUrlsList extends React.Component {

  render() {
    const formatOriginalUrl = (originalUrl) => {
      if (originalUrl.length > 50) {
        return originalUrl.substring(0, 50) + "...";
      }
      return originalUrl;
    }

    const itemListLI = this.props.urlList.sort((a,b) => moment(b.createdAt) - moment(a.createdAt)).map((item) =>
      <li key={item.id}>
        <div className="url-detail-grid">
          <div className="url-and-copy">
            <a target="noopener" href={HOST + item.shortedPath} className="url-detail-traceable">
              {HOST + item.shortedPath}
            </a>
            <CopyToClipboard text={HOST + item.shortedPath}>
              <input className="copy-to-clip" type="button" value="copy"/>
            </CopyToClipboard>
          </div>
          <span className="url-detail-original">{formatOriginalUrl(item.originalUrl)}</span>
          <TraylMetrics urlId={item.id}/>
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
