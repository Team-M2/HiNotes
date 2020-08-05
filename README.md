# HiNotes

## :notebook_with_decorative_cover: Introduction 
Huawei's is an advanced reference note application created with HMS kits for phones running with the Android-based HMS service. It was developed with the Kotlin language.

## :iphone: Screenshots 
<table>
  <tbody>
    <tr>
      <td><img src="https://user-images.githubusercontent.com/11235344/88550102-cbd21200-d029-11ea-9499-4341c63175f7.jpg"></td>
      <td><img src="https://user-images.githubusercontent.com/11235344/88550120-d4c2e380-d029-11ea-9c70-29f01681141f.jpg"></td>
      <td><img src="https://user-images.githubusercontent.com/11235344/88550272-0c319000-d02a-11ea-89eb-35e9f0e4f15b.jpg"></td>
    </tr>
    <tr>
      <td><img src="https://user-images.githubusercontent.com/11235344/88550312-1b184280-d02a-11ea-8a87-1230dde58a36.jpg"></td>
      <td><img src="https://user-images.githubusercontent.com/11235344/88552784-29b42900-d02d-11ea-9ee7-47dd081133ad.gif"></td>
      <td><img src="https://user-images.githubusercontent.com/11235344/88552819-32a4fa80-d02d-11ea-8095-0ecb053a8128.gif"></td>
    </tr>
  </tbody>
 </table>
 
 ## :question: Before You Start 
 **You need to agconnect-services.json for run this project correctly.**

- If you don't have a Huawei Developer account check this document for create; https://developer.huawei.com/consumer/en/doc/start/10104
- Open your Android project and find Debug FingerPrint (SHA256) with follow this steps; View -> Tool Windows -> Gradle -> Tasks -> Android -> signingReport
- Login to Huawei Developer Console (https://developer.huawei.com/consumer/en/console)
- If you don't have any app check this document for create; https://developer.huawei.com/consumer/en/doc/distribution/app/agc-create_app
- Add SHA256 FingerPrint into your app with follow this steps on Huawei Console; My Apps -> Select App -> Project Settings
- Make enable necessary SDKs with follow this steps; My Apps -> Select App -> Project Settings -> Manage APIs
- For this project you have to set enable Map Kit, Site Kit, Auth Service, ML Kit
- Than go again Project Settings page and click "agconnect-services.json" button for download json file.
- Move to json file in base "app" folder that under your android project. (https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/69407812#h1-1577692046342)

## :milky_way: Features 
- Sign up & Sign In with Huawei Id, Email with Password or Email Code.
- Add, Edit, Delete Notes or To-Do List.
- Add Location Info, POI. Application sends notification when it comes to the marked location or POI.
- Take a photo of text or upload text image from your device. Quickly add the text in these photos to your note. You can add text to your note by voice.

## :telescope: Future Features 
* Share notes or to-do with somebody

## :wrench: Kits Used 
* [Auth Service](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-auth-service-introduction)
* [Account Kit](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/introduction-0000001050048870)
* [Location Kit](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/introduction-0000001050706106)
* [Map Kit](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/android-sdk-introduction-0000001050158633)
* [Site Kit](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/android-sdk-introduction-0000001050158571)
* [ML Kit](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/service-introduction-0000001050040017)



## :link: Useful Links 
* [Huawei Developers Medium Page](https://medium.com/huawei-developers)
* [Huawei Developers Forum](https://forums.developer.huawei.com/forumPortal/en/home)
