import React from 'react';
import ReactDOM from 'react-dom';
import router from './router/index';
import {Provider} from 'react-redux';
import { createStore, applyMiddleware } from 'redux';
import {hashHistory} from 'react-router';
import { syncHistoryWithStore, routerMiddleware } from 'react-router-redux';
import rootReducer from './store/reducer';
import promiseMiddleware from './middleware/redux-promise';
import md5 from 'js-md5'

React.Component.prototype.$md5 = md5

const configureStore = (initialState, history) =>{
    const middlewares = [
        promiseMiddleware({}),
        routerMiddleware(history)
    ];
    return applyMiddleware(...middlewares)(createStore)(rootReducer, initialState)
}

const store = configureStore({}, hashHistory);

// 增强history,完成react router与redux store的绑定
const history = syncHistoryWithStore(hashHistory, store)

ReactDOM.render(
    <Provider store={store}>
        {router(history)}
    </Provider>
    ,
    document.getElementById('root'));

