package com.example.supanonymous.focoaedes.authenticator;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;


import com.example.supanonymous.focoaedes.LoginActivity;
import com.example.supanonymous.focoaedes.helpers.AuthHelper;
import com.example.supanonymous.focoaedes.services.ApiService;

import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;

public class FocoAedesAccountAuthenticator extends AbstractAccountAuthenticator {

    public static final String PASSWORD = "password";
    public static final String TOKEN_TYPE = "tokenType";
    private ApiService apiService;
    private Context context;
    private AuthHelper callHelper;

    public FocoAedesAccountAuthenticator(Context ctx){
        super(ctx);
        context = ctx;
    }
    @Override
    public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String s) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        final Intent intent = new Intent(context, LoginActivity.class);
        // This key can be anything. Try to use your domain/package
        intent.putExtra("KEY_ACCOUNT_TYPE", accountType);
        // This key can be anything too. It's just a way of identifying the token's type (used when there are multiple permissions)
        intent.putExtra("KEY_AUTH_TYPE", authTokenType);
        // This key can be anything too. Used for your reference. Can skip it too.
        intent.putExtra("ADD_ACCOUNT", true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        final AccountManager accountManager = AccountManager.get(context);

        String authToken = accountManager.peekAuthToken(account, authTokenType);

        // Lets give another try to authenticate the user
        if (TextUtils.isEmpty(authToken)) {
            String password = accountManager.getPassword(account);
            if (password != null) {
                callHelper.refreshAuthToken(context, authToken);
                authToken = callHelper.refreshAuthToken(context, authToken);
            }
        }

        // If we get an authToken - we return it
        if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        // If we get here, then we couldn't access the user's password - so we
        // need to re-prompt them for their credentials. We do that by creating
        // an intent to display our LoginActivity.
        final Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra("KEY_ACCOUNT_TYPE", account.type);
//        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);
        intent.putExtra("KEY_AUTH_TYPE", authTokenType);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String s) {
        return "full_acess";
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] strings) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(KEY_BOOLEAN_RESULT, false);
        return result;
    }
}