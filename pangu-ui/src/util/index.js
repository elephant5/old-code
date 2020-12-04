import {getHttpPro} from './util'
// 对象数组去重
export const  unique = objArray => {
    let hash = {}; 
    objArray = objArray.reduce((item, next) => {
        // eslint-disable-next-line no-unused-expressions
        hash[next.rule]? '': hash[next.rule] = true && item.push(next);
        return item;
    }, []);
    return objArray;
}

// fileList转化
export const getFileList = list => {
    const result = list.map(item => {
        const pgCdnNoHttpFullUrl = item.pgCdnNoHttpFullUrl;
        const pgCdnHttpUrl = item.pgCdnHttpUrl;
        const guid = item.guid;
        const ext = item.ext;
        // const url = `${pgCdnHttpUrl}/${guid}.${ext}`;
        const url = `${getHttpPro()}${pgCdnNoHttpFullUrl}`;
        item.url = url;
        item.uid = item.id;
        item.status = 'done';
        return item;
    })
    return result;
}