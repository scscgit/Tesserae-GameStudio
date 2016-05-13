var auth2 = {};

function renderUserInfo(googleUser, htmlElmId) {
    var profile = googleUser.getBasicProfile();
    var htmlString = "<p>Používateľ prihlásený</p>\n<ul>";
    //Toto id sa nema pouzivat na komunikaciu s vlastnym serverom
    htmlString += "\n <li> ID: " + profile.getId();
    htmlString += "\n <li>  meno: " + profile.getName();
    htmlString += "\n <li>  Krstné meno: " + profile.getGivenName();
    htmlString += "\n <li>  Priezvisko: " + profile.getFamilyName();
    htmlString += "\n <li>  URL obrázka: " + profile.getImageUrl();
    htmlString += "\n <li>  Email: " + profile.getEmail();
    htmlString += "\n</ul>";
    document.getElementById(htmlElmId).innerHTML = htmlString;
}

function renderLogOutInfo(htmlElmId) {
    var htmlString = "<p>Používateľ nie je prihlásený</p>";
    document.getElementById(htmlElmId).innerHTML = htmlString;
}

function signOut() {
    if (auth2.signOut) auth2.signOut();
    if (auth2.disconnect) auth2.disconnect();
}

function userChanged(user) {
    document.getElementById("userName").innerHTML = user.getBasicProfile().getName();

    var userInfoElm = document.getElementById("userStatus");

    if (userInfoElm) {
        renderUserInfo(user, "userStatus");
    }
}

var updateSignIn = function () {
    var sgnd = auth2.isSignedIn.get();
    if (sgnd) {
        document.getElementById("SignInButton").classList.add("skryty");
        //document.getElementById("SignedIn").classList.remove("skryty");
        document.getElementById("userName").innerHTML = auth2.currentUser.get().getBasicProfile().getName();

        console.log('Google Signed In Event');
        if (googleSignedInEvent) {
            googleSignedInEvent();
        }
    } else {
        document.getElementById("SignInButton").classList.remove("skryty");
        //document.getElementById("SignedIn").classList.add("skryty");

        console.log('Google Signed Out Event');
        if (googleSignedOutEvent) {
            googleSignedOutEvent();
        }
    }

    var userInfoElm = document.getElementById("userStatus");

    if (userInfoElm) {
        if (sgnd) {
            renderUserInfo(auth2.currentUser.get(), "userStatus");
        } else {
            renderLogOutInfo("userStatus");
        }
    }
};

function startGSingIn() {
    gapi.load('auth2', function () {
        gapi.signin2.render('SignInButton', {
            'width': 220,
            'height': 50,
            'longtitle': true,
            'theme': 'dark',
            'onsuccess': onSuccess,
            'onfailure': onFailure
        });

        //zavolat po inicializácii OAuth 2.0
        gapi.auth2.init().then(
            function () {
                console.log('OAuth 2.0 init');
                auth2 = gapi.auth2.getAuthInstance();
                auth2.currentUser.listen(userChanged);
                auth2.isSignedIn.listen(updateSignIn);
                //tiez po inicializacii
                auth2.then(updateSignIn);
            });
    });
}

function onSuccess(googleUser) {
    //Reading the user data
    var profile = googleUser.getBasicProfile();

    //Filtering the reqired data
    var id = profile.getId();
    var name = profile.getName();
    var givenName = profile.getGivenName();
    var familyName = profile.getFamilyName();
    var imageUrl = profile.getImageUrl();
    var email = profile.getEmail();

    //ID sa nema pouzivat na komunikaciu so serverom, takze v pripade rozsirovania aplikacie
    //bude potrebne nacitavat ho pomocou overenych sposobov na zaklade Google OAuth navodu
    $('.Gid > input').val(id);
    $('.GnickName > input').val(name);
    if (familyName != null) {
        $('.GrealName > input').val(givenName + " " + familyName);
    } else {
        $('.GrealName > input').val(givenName);
    }

    $('.Gemail > input').val(email);
    $('.Gimage > input').val(imageUrl);

    //PrimeFaces RemoteCommand call
    onSuccessGoogleController(
        // [{
        //     id: id,
        //     name: name,
        //     givenName: givenName,
        //     familyName: familyName,
        //     imageUrl: imageUrl,
        //     email: email
        // }]
    );

    //Logging updated state
    console.log('Logged in as: ' + name);
}
function onFailure(error) {
    //Logging error
    console.log(error);
}
