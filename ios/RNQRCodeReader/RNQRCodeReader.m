//
//  RNQRCodeReader.m
//  reactnativeqrcodereader
//
//  Created by Phu Master on 12/1/17.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTConvert.h>
#import "RNQRCodeReader.h"
#import "QRCodeReaderViewController.h"
#import "QRCodeReader.h"

@implementation RNQRCodeReader

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(scan:(NSDictionary *)options successCallback:(RCTResponseSenderBlock)successCallback errorCallback:(RCTResponseSenderBlock)errorCallback) {
  NSString *cancelText = [RCTConvert NSString:options[@"cancelText"]];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self scanAction:self cancelText:cancelText successCallback:successCallback errorCallback:errorCallback];
      });
}

- (IBAction)scanAction:(id)sender cancelText:(NSString*)cancelText successCallback:(RCTResponseSenderBlock)successCallback errorCallback:(RCTResponseSenderBlock)errorCallback
{
  if ([QRCodeReader supportsMetadataObjectTypes:@[AVMetadataObjectTypeQRCode]]) {
    UIViewController *rootVC = [UIApplication sharedApplication].delegate.window.rootViewController;
    static QRCodeReaderViewController *vc = nil;
    static dispatch_once_t onceToken;
    
    dispatch_once(&onceToken, ^{
      QRCodeReader *reader = [QRCodeReader readerWithMetadataObjectTypes:@[AVMetadataObjectTypeQRCode]];
      vc                   = [QRCodeReaderViewController readerWithCancelButtonTitle:cancelText codeReader:reader startScanningAtLoad:YES showSwitchCameraButton:YES showTorchButton:YES];
      vc.modalPresentationStyle = UIModalPresentationFormSheet;
    });
    vc.delegate = self;
    
    [vc setCompletionWithBlock:^(NSString *resultAsString) {
      if (resultAsString != NULL) {
        successCallback(@[resultAsString]);
      } else {
        errorCallback(@[[NSNull null]]);
      }
      NSLog(@"Completion with result: %@", resultAsString);
    }];
//    dispatch_async(dispatch_get_main_queue(), ^{
//      [rootVC presentViewController:vc animated:YES completion:NULL];
//    });
    [rootVC presentViewController:vc animated:YES completion:NULL];
  }
  else {
    errorCallback(@[[NSNull null]]);
  }
}

- (void)reader:(QRCodeReaderViewController *)reader didScanResult:(NSString *)result
{
  [reader stopScanning];
//  UIViewController *rootVC = UIApplication.sharedApplication.keyWindow.rootViewController;
  UIViewController *rootVC = [UIApplication sharedApplication].delegate.window.rootViewController;
  [rootVC dismissViewControllerAnimated:YES completion:^{
//    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"QRCodeReader" message:result delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
//    [alert show];
  }];
}

- (void)readerDidCancel:(QRCodeReaderViewController *)reader
{
  UIViewController *rootVC = [UIApplication sharedApplication].delegate.window.rootViewController;
  [rootVC dismissViewControllerAnimated:YES completion:NULL];
}

@end
