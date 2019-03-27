It started as an initiative to lend a helping hand to status holders at various government agencies. To organize documents and easily share them. We have been on the radio and TV at Omroep Brabant and have had many nice reactions. Unfortunately we have decided to stop developing the app. We will remove the app from the App Store shortly, but all the information and the app will remain available here on GitHub. Do you have any questions? You can still get in touch with the deverlopers via info@nlhelpu.nl.

# NL Help U

NL Help U is an app to help asylum seekers who are permitted to stay in The Netherlands to find their way in the Dutch society.

<img src="https://github.com/devolksbank/NL-Help-U/raw/develop/screenshots/Screenshot_1501075574.png" width="200"> <img src="https://github.com/devolksbank/NL-Help-U/raw/develop/screenshots/Screenshot_1501076733.png" width="200"> <img src="https://github.com/devolksbank/NL-Help-U/raw/develop/screenshots/Screenshots%20-%202017-08-08%20-%20AR/10-section-info.png" width="200">

## Where can I try the app?

Since the app will be removed from the Android Play Store, you can build the app from the source code.

Only an Android app is available, no other operating systems are supported.

## How does it work?

The app contains information about several services like DigiD, social wealthfare payments, bank accounts, health insurance. 
This helps in finding out what kind of things are to be considered, and how they can be arranged.

A lot of these services require a number of documents to be provided for applying to them. 
The app allows the user to take pictures of the documents and store them locally on the phone. 
This keeps track of the documents and allows them to be emailed in order for the application to be processed. 
This can either be done by the user, or by the 'assistant' of the user.

> Note that no documents are stored on the internet whatsoever! All content is only available to the app itself.

## How can I get it?

The app will shortly be removed from the Android Play store. The source code will remain available here on Github.

## How can I contribute?

You can take the code to revive the app or just use the code for development on your own app.

Please feel free to create some pullrequests or contact us at: [info@nlhelpu.nl](info@nlhelpu.nl)

## Howto start development

When opening the app, you might get a message regarding missing properties (like: NL_HELP_U_RELEASE_STORE_FILE). To fix this, please add a file with the following content in [home-directory]/.gradle/gradle.properties:
```
NL_HELP_U_RELEASE_STORE_FILE=<path-to-a-valid-possible-empty-file>
NL_HELP_U_RELEASE_STORE_PASSWORD=
NL_HELP_U_RELEASE_KEY_ALIAS=
NL_HELP_U_RELEASE_KEY_PASSWORD=
```

# License

MIT
