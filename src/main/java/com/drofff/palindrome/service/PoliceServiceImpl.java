package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Department;
import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.PoliceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.EntityUtils.copyNonEditableFields;
import static com.drofff.palindrome.utils.ValidationUtils.*;

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
		validate(police);
		validatePhoto(photo);
		validateDepartmentId(police.getDepartmentId());
		User currentUser = getCurrentUser();
		validateIsPolice(currentUser);
		validateHasNoPoliceProfile(currentUser);
		String photoUri = photoService.savePhotoForUser(photo, currentUser);
		police.setPhotoUri(photoUri);
		police.setUserId(currentUser.getId());
		policeRepository.save(police);
	}

	private void validateIsPolice(User user) {
		if(isNotPolice(user)) {
			throw new ValidationException("The user should obtain a role of a police officer");
		}
	}

	private boolean isNotPolice(User user) {
		return !user.isPolice();
	}

	private void validateHasNoPoliceProfile(User user) {
		if(hasPoliceProfile(user)) {
			throw new ValidationException("User already has police profile");
		}
	}

	@Override
	public void updatePoliceProfile(Police police) {
		validate(police);
		validateDepartmentId(police.getDepartmentId());
		Police actualPolice = getActualPoliceProfile(police);
		copyNonEditableFields(actualPolice, police);
		policeRepository.save(police);
	}

	private void validateDepartmentId(String departmentId) {
		if(notExistsDepartmentWithId(departmentId)) {
			throw new ValidationException("Department with such id doesn't exist");
		}
	}

	private boolean notExistsDepartmentWithId(String id) {
		return !departmentService.existsDepartmentWithId(id);
	}

	private Police getActualPoliceProfile(Police police) {
		User currentUser = getCurrentUser();
		if(currentUser.isAdmin()) {
			return getPoliceById(police.getId());
		}
		return getPoliceByUserId(currentUser.getId());
	}

	@Override
	public void updatePolicePhoto(MultipartFile photo) {
		User currentUser = getCurrentUser();
		validateHasPoliceProfile(currentUser);
		validatePhoto(photo);
		String photoUri = photoService.savePhotoForUser(photo, currentUser);
		Police police = getPoliceByUserId(currentUser.getId());
		police.setPhotoUri(photoUri);
		policeRepository.save(police);
	}

	private void validateHasPoliceProfile(User user) {
		if(hasNoPoliceProfile(user)) {
			throw new ValidationException("User should obtain a police profile");
		}
	}

	private void validatePhoto(MultipartFile photo) {
		validateNotNull(photo, "Photo should be provided");
	}

	@Override
	public boolean hasNoPoliceProfile(User user) {
		return !hasPoliceProfile(user);
	}

	private boolean hasPoliceProfile(User user) {
		return policeRepository.findByUserId(user.getId()).isPresent();
	}

	@Override
	public void assignAccessTokenToPoliceWithId(String accessToken, String policeId) {
		validateNotNull(accessToken, "Access token should not be null");
		Police police = getPoliceById(policeId);
		police.getAccessTokens().add(accessToken);
		policeRepository.save(police);
	}

	@Override
	public Police getPoliceById(String id) {
		validateNotNull(id, "Missing police id");
		return policeRepository.findById(id)
				.orElseThrow(() -> new ValidationException("Police with such id doesn't exist"));
	}

	@Override
	public void enableTwoStepAuth() {
		User currentUser = getCurrentUser();
		Police police = getPoliceByUserId(currentUser.getId());
		validateHasTwoStepAuthDisabled(police);
		police.setTwoStepAuthEnabled(true);
		policeRepository.save(police);
	}

	private void validateHasTwoStepAuthDisabled(Police police) {
		if(police.isTwoStepAuthEnabled()) {
			throw new ValidationException("Two step authentication is already enabled");
		}
	}

	@Override
	public void disableTwoStepAuth() {
		User currentUser = getCurrentUser();
		Police police = getPoliceByUserId(currentUser.getId());
		validateHasTwoStepAuthEnabled(police);
		police.setTwoStepAuthEnabled(false);
		policeRepository.save(police);
	}

	private void validateHasTwoStepAuthEnabled(Police police) {
		if(hasTwoStepAuthDisabled(police)) {
			throw new ValidationException("Two step authentication is already disabled");
		}
	}

	private boolean hasTwoStepAuthDisabled(Police police) {
		return !police.isTwoStepAuthEnabled();
	}

	@Override
	public Police getPoliceByUserId(String id) {
		validateNotNull(id, "User id is required");
		return policeRepository.findByUserId(id)
				.orElseThrow(() -> new ValidationException("User with such id has no police profile"));
	}

	@Override
	public boolean hasAnyPoliceFromDepartment(Department department) {
		validateNotNullEntityHasId(department);
		return policeRepository.existsByDepartmentId(department.getId());
	}

}
