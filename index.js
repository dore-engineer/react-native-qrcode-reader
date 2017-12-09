import {NativeModules, Platform} from 'react-native';

const {RNQRCodeReader} = NativeModules;

const DEFAULT_OPTIONS = {
  cancelText: 'Cancel',
  placeholder: 'Place qrcode to square',
  title: 'QRCode scan',
  errorText: 'This device is not support'
};

export default {
  scan: function (opts) {
    const options = opts || DEFAULT_OPTIONS;
    return new Promise(function (resolve, reject) {

      if (Platform.OS === 'ios') {
        RNQRCodeReader.scan(options, function (successCallbackData) {
          return resolve(successCallbackData);
        }, function (errorOrNullCallbackData) {
          return reject(errorOrNullCallbackData);
        });
      } else {
        try {
          RNQRCodeReader.scan(options).then(function (successCallbackData) {
            return resolve(successCallbackData);
          }).catch(function (errorOrNullCallbackData) {
            return reject(errorOrNullCallbackData);
          })
        } catch (e) {
          return reject(e);
        }
      }

    });
  }
}