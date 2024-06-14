package hr.irb.CIR.Auth;

interface IAuth {
    // public static function emailCheck($email);
    // public function setTimeOut($seconds);
    // public static function validateUrl($url);
    // public function setActivity();
    public String getLogin();
    // public function authenticate($login, $pass);
    // public function login($login, $pass);
    public boolean isLoggedIn();
    // public function isAdmin();
    public void logOut();
    // public function lockUser($userId, $reason);
    // public function unLockUser($userId);
    // public function validateByEmail($validationString, $eml);
    // public function generateValidationStringForLoginOrEmail($loginOrEmail);
    // public function createUser(AuthUserData $userData);
    // public function deleteUser($userData);
    // public function updateUser($userData);
    public String getUserId();
    // public function passwordReset($userId, $currentPass, $pass, $passRepeat);
}
