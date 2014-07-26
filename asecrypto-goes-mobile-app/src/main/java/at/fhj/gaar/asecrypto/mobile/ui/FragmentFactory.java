package at.fhj.gaar.asecrypto.mobile.ui;

import android.app.Fragment;
import android.os.Bundle;

import at.fhj.gaar.asecrypto.mobile.ui.apptasks.BaseFragment;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.bezout.BezoutFragment;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.euclid.EuclidFragment;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.exponentiation.FastExponentiationFragment;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.exponentiation.SlowExponentiationFragment;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.fermat.FermatTestFragment;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.numbercounter.NumberCounterFragment;
import at.fhj.gaar.asecrypto.mobile.ui.navigation.DrawerItemIdentifiers;

/**
 * Creates the corresponding Fragments when given the task id.
 */
public class FragmentFactory {

    public static Fragment getFragment(int taskId, String taskName) {
        BaseFragment createdFragment;

        switch (taskId) {
            case DrawerItemIdentifiers.TASK_NUMBER_COUNTER:
                createdFragment = new NumberCounterFragment();
                break;

            case DrawerItemIdentifiers.TASK_EUCLID:
                createdFragment = new EuclidFragment();
                break;

            case DrawerItemIdentifiers.TASK_BEZOUT:
                createdFragment = new BezoutFragment();
                break;

            case DrawerItemIdentifiers.TASK_SLOW_EXPONENTATION:
                createdFragment = new SlowExponentiationFragment();
                break;

            case DrawerItemIdentifiers.TASK_FAST_EXPONENTATION:
                createdFragment = new FastExponentiationFragment();
                break;

            case DrawerItemIdentifiers.TASK_FERMAT_TEST:
                createdFragment = new FermatTestFragment();
                break;

            case DrawerItemIdentifiers.TASK_MILLER_RABIN_TEST:
                createdFragment = new BaseFragment();
                break;

            case DrawerItemIdentifiers.TASK_RSA_CHINESE_REMAINDER:
                createdFragment = new BaseFragment();
                break;

            case DrawerItemIdentifiers.TASK_PRIMITIVE_ROOTS:
                createdFragment = new BaseFragment();
                break;

            default:
                createdFragment = new BaseFragment();
                break;
        }

        return setUpTaskFragment(createdFragment, taskId, taskName);
    }

    private static Fragment setUpTaskFragment(BaseFragment fragment, int taskId, String taskName) {
        Bundle args = new Bundle();
        args.putInt(BaseFragment.ARG_SECTION_NUMBER, taskId);
        args.putString(BaseFragment.ARG_SECTION_TITLE, taskName);

        fragment.setArguments(args);
        return fragment;
    }
}
