//
//  MICShouldUpdate.m
//  hot
//
//  Created by necfol on 2017/7/31.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "MICShouldUpdate.h"

@implementation MICShouldUpdate

+(void)shouldUpdate:(CallbackBlock)callback {
  NSString *url = @"http://192.168.100.75:3000";
  NSMutableURLRequest *newRequest = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:url]];
  [newRequest setHTTPMethod:@"GET"];
  [NSURLConnection sendAsynchronousRequest:newRequest queue:[NSOperationQueue mainQueue] completionHandler:^(NSURLResponse * response, NSData * data, NSError * connectionError) {
    if(connectionError == nil){
      NSError *error;
      NSDictionary *jsonDic = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
      if(!error){
        NSString *absolutepath = [[NSBundle mainBundle] pathForResource:@"Version" ofType:@"plist"];
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] initWithContentsOfFile:absolutepath];
        NSString *currentVersion = [dict objectForKey:@"version"];
        NSString *newVersion = jsonDic[@"version"];
        if(![currentVersion isEqualToString:newVersion]){
          callback(1,jsonDic);
        }else{
          callback(0,nil);
        }
      }else{
        callback(0,nil);
      }
    }
  }];
}
@end
