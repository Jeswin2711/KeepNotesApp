package com.bridgelabz.notesapp.notes.service;

import com.bridgelabz.notesapp.exception.CustomException;
import com.bridgelabz.notesapp.notes.dto.NotesDto;
import com.bridgelabz.notesapp.notes.model.Notes;
import com.bridgelabz.notesapp.notes.repository.NotesRepository;
import com.bridgelabz.notesapp.notes.service.interfaces.INotesService;
import com.bridgelabz.notesapp.user.repository.UserRepository;
import com.bridgelabz.notesapp.utility.Response;
import com.bridgelabz.notesapp.utility.security.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

@Service
public class INotesServiceImpl implements INotesService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private HttpServletRequest httpServlet;

    @Autowired
    private JwtService jwtService;

    public String tokenCheck() {
        String authorizationHeader = httpServlet.getHeader("Authorization");
        String jwt = authorizationHeader.substring(7);
        String userName = jwtService.extractUsername(jwt);
        return userName;
    }

    public Response addNoteById(int id, NotesDto notesDto) throws CustomException {
        Notes notes = mapper.map(notesDto, Notes.class);
        String userName = tokenCheck();
        if (userRepository.findById(id).get().getUserName().equals(userName)) {
            userRepository.findById(id).get().getNotes().add(notes);
            notesRepository.save(notes);
        } else {
            throw new CustomException("ID and Token Not Matching");
        }
        return new Response("Note Added Successfully", HttpStatus.OK);
    }

    public Response getNoteById(int user_id, int note_id) throws CustomException {
        boolean flag = false;
        if (userRepository.findById(user_id).isPresent()) {
            List<Notes> userNotes = userRepository.findById(user_id).get().getNotes();
            for (Notes note : userNotes) {
                if (note.getId() == note_id) {
                    flag = true;
                }
            }
            if (!false) {
                System.out.println("Note ID Present");
            } else {
                throw new CustomException("NOTE ID Not Found");
            }
        } else {
            throw new CustomException("Invalid User Id");
        }
        NotesDto notesDto = mapper.map(notesRepository.getById(note_id), NotesDto.class);
        return new Response("Note ID : " + note_id, notesDto);
    }

    public Response getNotesById(int user_id) throws CustomException {
        List<Notes> userNotes;
        List<Notes> displayNotes = new ArrayList<>();

        String userName = tokenCheck();

        if (userRepository.findByUserName(userName).get().getUserName().equals(userName)) {
            userNotes = userRepository.findById(user_id).get().getNotes();
            for (Notes note : userNotes) {
                if (note.isArchived() == false && note.isPinned() == false && note.isDeleted() == false) {
                    displayNotes.add(note);
                }
            }
        } else {
            throw new CustomException("Invalid User Id");
        }
        return new Response("User Id : " + user_id, displayNotes);
    }

    public Response deleteNoteById(int user_id, int note_id) throws CustomException {
        if (userRepository.findById(user_id).isPresent()) {
            List<Notes> userNotes = userRepository.findById(user_id).get().getNotes();
            for (Notes note : userNotes) {
                if (notesRepository.findById(note_id).isPresent()) {
                    if (note.getId() == note_id) {

                        if (notesRepository.findById(note_id).get().isArchived() == true) {
                            notesRepository.findById(note_id).get().setArchived(false);
                        }
                        if (notesRepository.findById(note_id).get().isDeleted() == false) {
                            notesRepository.findById(note_id).get().setDeleted(true);
                            notesRepository.save(notesRepository.findById(note_id).get());
                        } else if (notesRepository.findById(note_id).get().isPinned() == true) {
                            notesRepository.findById(note_id).get().setPinned(false);
                            notesRepository.save(notesRepository.findById(note_id).get());
                        } else if (notesRepository.findById(note_id).get().isDeleted() == true) {
                            notesRepository.deleteById(note_id);
                        } else {
                            throw new CustomException("No data Found for this Id");
                        }
                    }
                } else {
                    throw new CustomException("Note Id not found...");
                }
            }
        } else {
            throw new CustomException("Invalid User Id");
        }
        return new Response("Deleted Successfully Note ID : " + note_id, HttpStatus.OK);
    }

    public Response deleteNote(int user_id, int note_id) throws CustomException {
        String userName = tokenCheck();

        if (userRepository.findById(user_id).get().getUserName().equals(userName)) {
            if (notesRepository.findById(note_id).isPresent()) {
                notesRepository.deleteById(note_id);
            } else {
                throw new CustomException("Note Not Found");
            }
        } else {
            throw new CustomException("Invalid User Id");
        }
        return new Response("Note Removed Successfully : " + note_id, HttpStatus.OK);
    }

    public Response restoreNote(int user_id, int note_id) {
        String userName = tokenCheck();

        if (userRepository.findById(user_id).get().getUserName().equals(userName)) {
            if (notesRepository.findById(note_id).get().isDeleted() == true) {
                List<Notes> userNotes = userRepository.findById(user_id).get().getNotes();
                for (Notes note : userNotes) {
                    if (note.getId() == note_id) {
                        notesRepository.findById(note_id).get().setDeleted(false);
                        notesRepository.save(notesRepository.findById(note_id).get());
                    }
                }
            } else {
                throw new CustomException("Id is Not Deleted");
            }
        } else {
            throw new CustomException("Invalid User Id");
        }
        return new Response("Undo Successfull : " + note_id, HttpStatus.OK);
    }

    public Response archivedNote(String username, int note_id) {
        String userName = tokenCheck();

        if (userRepository.findByUserName(userName).get().getUserName().equals(userName)) {
            if (notesRepository.findById(note_id).get().isArchived() == false) {
                notesRepository.findById(note_id).get().setArchived(true);
                notesRepository.save(notesRepository.findById(note_id).get());
            } else {
                notesRepository.findById(note_id).get().setArchived(false);
                notesRepository.save(notesRepository.findById(note_id).get());
            }
        } else {
            throw new CustomException("Username Not Found");
        }

        return new Response("Archive Request Successfull", HttpStatus.OK);

    }

    public Response updateNote(String username, int note_id, NotesDto notesDto) {
        String userName = tokenCheck();
        if (username.equals(userName)) {
            Notes notes = notesRepository.getById(note_id);
            if (notes != null) {
                notes.setTitle(notesDto.getTitle());
                notes.setDescription(notesDto.getDescription());
                notes.setColor(notesDto.getColor());
                notesRepository.save(notes);
            } else {
                throw new CustomException("Note Not found");
            }
        } else
            throw new CustomException("You are Not Authorized");

        return new Response("Note Updated Successfully", HttpStatus.OK);
    }

    public Response getArchievedNotes(int user_id) {

        String userName = tokenCheck();
        List<Notes> archievedNotes = new ArrayList<>();

        if (userName.equals(userRepository.findById(user_id).get().getUserName())) {

            List<Notes> userNotes = userRepository.findById(user_id).get().getNotes();

            for (Notes note : userNotes) {
                if (note.isArchived() == true) {
                    archievedNotes.add(note);
                }
            }
        }
        // } else
        // throw new CustomException("YOU ARE NOT AUTHORIZED");

        return new Response("", archievedNotes);

    }

    public Response getTrashNotes(int user_id) {
        String userName = tokenCheck();

        List<Notes> deleteNotes = new ArrayList<>();

        if (userName.equals(userRepository.findById(user_id).get().getUserName())) {
            // System.out.println("===working123");
            // System.out.println("123456" + notesRepository.findUnArchived());

            List<Notes> userNotes = userRepository.findById(user_id).get().getNotes();

            for (Notes note : userNotes) {
                if (note.isDeleted() == true) {
                    deleteNotes.add(note);
                }
            }
        }

        return new Response("", deleteNotes);
        // } else
        // throw new CustomException("YOU ARE NOT AUTHORIZED");
    }

    public Response pinNote(int id) {
        String userName = tokenCheck();

        if (userRepository.findByUserName(userName).isPresent()) {
            if (notesRepository.findById(id).get().isPinned() == false) {
                notesRepository.findById(id).get().setPinned(true);
                notesRepository.save(notesRepository.findById(id).get());
            } else {
                notesRepository.findById(id).get().setPinned(false);
                notesRepository.save(notesRepository.findById(id).get());
            }
        }
        return new Response("Pinned Successfully", HttpStatus.OK);
    }

    public Response getPinned(int user_id) {

        String userName = tokenCheck();

        List<Notes> pinnedNotes = new ArrayList<>();

        if (userRepository.findByUserName(userName).isPresent()) {
            List<Notes> userNotes = userRepository.findById(user_id).get().getNotes();

            for (Notes note : userNotes) {
                if (note.isPinned() == true) {
                    pinnedNotes.add(note);
                }
            }
        }

        return new Response("OK", pinnedNotes);
    }
}