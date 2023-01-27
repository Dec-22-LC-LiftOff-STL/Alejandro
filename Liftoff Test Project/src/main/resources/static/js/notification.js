(async() => {

 const showNotification = () => {
    //create notification
    const notification = new Notification('LiftStyle Notification', {
        body: 'Hi, did you journal today?',
    });

    //close notification after 10 seconds
    setTimeout(() => {
        notification.close();
    }, 10*1000);

    //show an error message
    const showError = () => {
        const error = document.querySelector('.error');
        error.style.display = 'block';
        error.textContent = 'You blocked the notification';
    }

    //check notification permission
    let granted = false;

    if(Notification.permission === 'granted'){
        granted=true;
    }else if (Notification.permission !== 'denied'){
        let permission = await Notification.requestPermission();
        granted = permission === 'granted' ? true : false;
    }

    //show notification error
    granted ? showNotification() : showError;

};
});
