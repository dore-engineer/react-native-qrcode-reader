//
//  RNQRCodeReader.h
//  reactnativeqrcodereader
//
//  Created by Phu Master on 12/1/17.
//  Copyright © 2017 Facebook. All rights reserved.
//

#ifndef RNQRCodeReader_h
#define RNQRCodeReader_h

#import <React/RCTBridgeModule.h>
#import "QRCodeReaderDelegate.h"

@interface RNQRCodeReader : NSObject <QRCodeReaderDelegate, RCTBridgeModule>

- (IBAction)scanAction:(id)sender cancelText:(NSString*)cancelText successCallback:(RCTResponseSenderBlock)successCallback errorCallback:(RCTResponseSenderBlock)errorCallback;

@end
#endif /* RNQRCodeReader_h */
