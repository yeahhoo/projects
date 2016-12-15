import React, { Component, PropTypes } from 'react';

class RestResultFormComponent extends Component {

    constructor(props) {
        super(props);
        this.createMarkup = this.createMarkup.bind(this);
    }

    createMarkup(html) {
        return {__html: html};
    }

    render() {
        return (
            <div>
                {this.props.aboutMsg !== ''
                    ? <div dangerouslySetInnerHTML={this.createMarkup(this.props.aboutMsg)} />
                    : this.props.contactMsg !== ''
                        ? <div dangerouslySetInnerHTML={this.createMarkup(this.props.contactMsg)} />
                        : this.props.usernameMsg !== '' || this.props.serverMsg !== ''
                            ?
                                <div>
                                    <div className="col-sm-12">
                                        <label className="col-sm-3">Username</label>
                                        <span id="userPlaceholder">{this.props.usernameMsg}</span>
                                    </div>
                                    <div className="col-sm-12">
                                        <label className="col-sm-3">Server Msg</label>
                                        <span id="serverPlaceholder">{this.props.serverMsg}</span>
                                    </div>
                                </div>
                            : <div>Welcome stranger, we have React, REST and a little logic.</div>
                }
            </div>
        );
    }
}

RestResultFormComponent.propTypes = {
    aboutMsg: PropTypes.string.isRequired,
    contactMsg: PropTypes.string.isRequired,
    serverMsg: PropTypes.string.isRequired,
    usernameMsg: PropTypes.string.isRequired
}

export default RestResultFormComponent