import {fromJS} from 'immutable';
import _ from 'lodash';

// 获取单纯Obj类型的reducer
export function getObjReducer(state, payload, isData){
    let result = {};
    if(!_.isEmpty(payload)){
        if(isData){
            result = _.assign({}, result, payload);
        }else{
            result = _.assign({}, result, !_.isEmpty(payload.data) ? payload.data : {});
        }
    }
    return fromJS(result);
}

// 获取单纯Array类型的reducer
export function getArrayReducer(state, payload, isData){
    let result = [];
    if(!_.isEmpty(payload)){
        if(isData){
            result = payload;
        }else{
            result = !_.isEmpty(payload.data) ? payload.data.result : [];
        }
    }
    return fromJS(result);
}

// 获取merge obj类型的reducer
export function getMergeReducer(state, payload, meta){
    let result = {};
    if(!_.isEmpty(payload)){
        const {data = []} = payload;
        result[meta] = data || [];
    }
    return fromJS(result);
}
