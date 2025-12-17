package org.yearup.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;

import java.security.Principal;

@RestController
@RequestMapping("profile")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ProfileController {

    private final ProfileDao profileDao;
    private final UserDao userDao;

    public ProfileController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }


    @GetMapping()
    public Profile getProfile(Principal principal){

        int userId=userDao.getIdByUsername(principal.getName());

        return profileDao.getProfile(userId);
    }

    @PutMapping()
    public Profile updateProfile(@RequestBody Profile profile, Principal principal){
        int userId=userDao.getIdByUsername(principal.getName());
        return profileDao.updateProfile(userId,profile);
    }

}
