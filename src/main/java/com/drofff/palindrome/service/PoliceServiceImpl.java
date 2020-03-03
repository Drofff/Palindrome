package com.drofff.palindrome.service;

import static com.drofff.palindrome.enums.Role.POLICE;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.ValidationUtils.validate;
import static com.drofff.palindrome.utils.ValidationUtils.validateCurrentUserHasRole;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.PoliceRepository;

@Service
public class PoliceServiceImpl implements PoliceService {

	private final PoliceRepository policeRepository;
	private final DepartmentService departmentService;
	private final PhotoService photoService;

	@Autowired
	public PoliceServiceImpl(PoliceRepository policeRepository, DepartmentService departmentService,
	                         PhotoService photoService) {
		this.policeRepository = policeRepository;
		this.departmentService = departmentService;
		this.photoService = photoService;
	}

	@Override
	public void createPoliceProfileWithPhoto(Police police, MultipartFile photo) {
		validateCurrentUserHasRole(POLICE);
		validate(police);
		validatePhoto(photo);
		validateDepartmentId(police.getDepartmentId());
		User currentUser = getCurrentUser();
		validateHasNoPoliceProfile(currentUser);
		String photoUri = photoService.savePhotoForUser(photo, currentUser);
		police.setPhotoUri(photoUri);
		police.setUserId(currentUser.getId());
		policeRepository.save(police);
	}

	private void validateHasNoPoliceProfile(User user) {
		if(hasPoliceProfile(user)) {
			throw new ValidationException("User already has police profile");
		}
	}

	private void validatePhoto(MultipartFile photo) {
		validateNotNull(photo, "Photo should be provided");
	}

	private void validateDepartmentId(String departmentId) {
		if(notExistsDepartmentWithId(departmentId)) {
			throw new ValidationException("Department with such id doesn't exist");
		}
	}

	private boolean notExistsDepartmentWithId(String id) {
		return !departmentService.existsDepartmentWithId(id);
	}

	@Override
	public boolean hasNoPoliceProfile(User user) {
		return !hasPoliceProfile(user);
	}

	private boolean hasPoliceProfile(User user) {
		return policeRepository.findByUserId(user.getId()).isPresent();
	}

	@Override
	public Police getPoliceByUserId(String id) {
		return policeRepository.findByUserId(id)
				.orElseThrow(() -> new ValidationException("User with such id has no police profile"));
	}

}