import React from 'react';
import { Layout, Icon } from 'antd';

const { Footer } = Layout;

const Foot = props => {
    return (
        <Footer style={{ textAlign: 'center' }}>
            {/* Copyright <Icon type="copyright" /> 2019 The Colourful China All Rights Reserved    沪ICP备13046660号-2 */}
        </Footer>
    )
} 

export default Foot;